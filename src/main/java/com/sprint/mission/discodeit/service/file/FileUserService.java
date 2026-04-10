package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileUserService implements UserService {
    private final Map<UUID, User> data;
    private final Path path = Path.of(System.getProperty("user.dir"), "data/user.ser");

    public FileUserService() {
        Map<UUID, User> temp;
        try {
            Files.createDirectories(path.getParent());
            if (Files.exists(path)) {
                try (FileInputStream fis = new FileInputStream(path.toFile());
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    temp = (Map<UUID, User>) ois.readObject();
                }
            } else temp = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            temp = new HashMap<>();
        }
        this.data = temp;
    }

    private void saveMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(data);
        } catch (Exception e) {
            throw new IllegalStateException("user file save failed.", e);
        }
    }

    @Override
    public boolean isUniqueUsername(String username) {
        if (username == null) throw new IllegalArgumentException("username is null.");

        return data.values().stream()
                .noneMatch(user -> Objects.equals(user.getUsername(), username));
    }

    @Override
    public boolean isUniqueEmail(String email) {
        if (email == null) throw new IllegalArgumentException("email is null.");

        return data.values().stream()
                .noneMatch(user -> Objects.equals(user.getEmail(), email));
    }

    @Override
    public User save(User user) {
        if (user == null) throw new IllegalArgumentException("user is null.");
        if (!isUniqueUsername(user.getUsername())) throw new IllegalStateException("username is duplicate.");
        if (!isUniqueEmail(user.getEmail())) throw new IllegalStateException("email is duplicate.");

        data.put(user.getId(), user);
        saveMap();
        return user;
    }

    @Override
    public User findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new NoSuchElementException("user not found."));
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("username is null.");

        return data.values().stream()
                .filter(user -> Objects.equals(user.getUsername(), username))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("user not found."));
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>(data.values());
        list.sort(Comparator.comparing(User::getUpdatedAt));
        return list;
    }

    @Override
    public boolean update(UUID srcUserId, UUID dstUserId, User userData) {
        if (srcUserId == null) throw new IllegalArgumentException("srcUserId is null.");
        if (dstUserId == null) throw new IllegalArgumentException("dstUserId is null.");
        if (userData == null) throw new IllegalArgumentException("userData is null.");

        if (userData.getUsername() == null) throw new IllegalArgumentException("username is null.");
        if (userData.getEmail() == null) throw new IllegalArgumentException("email is null.");
        if (userData.getPassword() == null) throw new IllegalArgumentException("password is null.");

        User loginUser = findById(srcUserId);

        if (!isUniqueUsername(userData.getUsername()) && !userData.getUsername().equals(loginUser.getUsername())) {
            throw new IllegalStateException("username is duplicate.");
        }

        if (!isUniqueEmail(userData.getEmail()) && !userData.getEmail().equals(loginUser.getEmail())) {
            throw new IllegalStateException("email is duplicate.");
        }

        if (!srcUserId.equals(dstUserId)) return false;

        loginUser.update(userData.getUsername(), userData.getEmail(), userData.getPassword());
        saveMap();
        return true;
    }

    @Override
    public boolean delete(UUID srcUserId, UUID dstUserId) {
        if (srcUserId == null) throw new IllegalArgumentException("srcUserId is null.");
        if (dstUserId == null) throw new IllegalArgumentException("dstUserId is null.");

        findById(srcUserId);

        if (!srcUserId.equals(dstUserId)) return false;

        data.remove(srcUserId);
        saveMap();
        return true;
    }
}
