package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findById(UUID id);
    User findByUsername(String username);
    List<User> findByNickname(String nickname);
    List<User> findAll();
    void delete(UUID id);
}
