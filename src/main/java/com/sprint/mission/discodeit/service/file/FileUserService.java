package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileUserService implements UserService {
    private final Map<UUID, User> data;
    private final Path path = Path.of(System.getProperty("user.dir"),"data/user.ser");

    public FileUserService() {
        Map<UUID, User> temp;
        try {
            Files.createDirectories(path.getParent());

            if(Files.exists(path))
            {
                try(FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)) {
                    temp = (Map<UUID, User>) ois.readObject();
                }
            }
            else temp = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            temp = new HashMap<>();
        }
        this.data = temp;
    }

    private void saveMap(){
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isUniqueUsername(String username) {
        if(username == null) throw new IllegalArgumentException("username is null.");

        for (User user : data.values()) {
            if(user.getUsername().equals(username)) return false;
        }
        return true;
    }

    @Override
    public boolean isUniqueEmail(String email) {
        if(email == null) throw new IllegalArgumentException("email is null.");

        for (User user : data.values()) {
            if(user.getEmail().equals(email)) return false;
        }
        return true;
    }

    @Override
    public User save(User user) {
        if (user == null) throw new IllegalArgumentException("user is null.");
        if (!isUniqueUsername(user.getUsername())) throw new IllegalArgumentException("username is duplicate.");
        if (!isUniqueEmail(user.getEmail())) throw new IllegalArgumentException("email is duplicate.");

        data.put(user.getId(), user);
        saveMap();
        return user;
    }

    @Override
    public User findById(UUID id) {
        if (id == null) throw new IllegalArgumentException("id is null.");

        return data.get(id);
    }

    @Override
    public User findByUsername(String username) {
        if(username == null) throw new IllegalArgumentException("username is null.");

//        return data.values().stream().filter(user -> user.getUsername().equals(username)).findFirst().orElse(null);
        for (User value : data.values()) {
            if(value.getUsername().equals(username)) return value;
        }
        return null;
    }

    @Override
    public List<User> findByNickname(String nickname) {
        if(nickname == null) throw new IllegalArgumentException("nickname is null.");

        List<User> list = new ArrayList<>();
        for (User value : data.values()) {
            if(value.getNickname().equals(nickname)) list.add(value);
        }
        list.sort(Comparator.comparing(User::getUpdatedAt));
        return list;
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
        if (userData.getNickname() == null) throw new IllegalArgumentException("nickname is null.");


        User loginUser = findById(srcUserId);
        if (loginUser == null) throw new IllegalArgumentException("src user not found.");

        if(!isUniqueUsername(userData.getUsername()) && !userData.getUsername().equals(loginUser.getUsername())) {
            throw new IllegalArgumentException("username is duplicate.");
        }

        if(!isUniqueEmail(userData.getEmail()) && !userData.getEmail().equals(loginUser.getEmail())) {
            throw new IllegalArgumentException("email is duplicate.");
        }

        if (!srcUserId.equals(dstUserId)) return false;

        loginUser.update(userData.getUsername(), userData.getEmail(), userData.getPassword(), userData.getNickname());
        saveMap();
        return true;
    }

    @Override
    public boolean delete(UUID srcUserId, UUID dstUserId) {
        if (srcUserId == null) throw new IllegalArgumentException("srcUserId is null.");
        if (dstUserId == null) throw new IllegalArgumentException("dstUserId is null.");

        User loginUser = findById(srcUserId);
        if (loginUser == null) throw new IllegalArgumentException("src user not found.");

        if (!srcUserId.equals(dstUserId)) return false;

        data.remove(srcUserId);
        saveMap();
        return true;
    }
}
