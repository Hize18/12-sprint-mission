package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final Map<UUID, User> data;
    private final Path path = Path.of(System.getProperty("user.dir"),"data_repo/user.ser");

    public FileUserRepository() {
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

    private void saveMap(Map<UUID, User> map){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))){
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
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public User findByUsername(String username) {
        for (User value : data.values()) {
            if(value.getUsername().equals(username)) return value;
        }
        return null;
    }

    @Override
    public List<User> findByNickname(String nickname) {
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
    public void delete(UUID id) {
        Map<UUID, User> temp = new HashMap<>(data);

        temp.remove(id);
        saveMap(temp);
        data.remove(id);
    }
}
