package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(name ="12-sprint-mission.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {
    private final Map<UUID, User> data;
    private final Path path;

    public FileUserRepository(
            @Value("${12-sprint-mission.repository.file-directory:data}") String directory
    ) {
        path = Path.of(System.getProperty("user.dir"), directory,"user.ser");

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

    private void saveMap(Map<UUID, User> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(map);
        } catch (Exception e) {
            throw new IllegalStateException("user file save failed.", e);
        }
    }

    @Override
    public User save(User user) {
        Map<UUID, User> temp = new HashMap<>(data);

        temp.put(user.getId(), user);
        saveMap(temp);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return data.values().stream()
                .filter(user -> Objects.equals(user.getUsername(), username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>(data.values());
        list.sort(Comparator.comparing(User::getUpdatedAt));
        return list;
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> temp = new HashMap<>(data);

        temp.remove(id);
        saveMap(temp);
        data.remove(id);
    }
}
