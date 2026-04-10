package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    boolean isUniqueUsername(String username);

    boolean isUniqueEmail(String email);

    User save(User user);

    User findById(UUID id);

    User findByUsername(String username);

    List<User> findAll();

    boolean update(UUID srcUserId, UUID dstUserId, User userData);

    boolean delete(UUID srcUserId, UUID dstUserId);
}