package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() {
        data = new HashMap<>();
    }

    @Override
    public User save(User user) {
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
        data.remove(id);
    }
}
