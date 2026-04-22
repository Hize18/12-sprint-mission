package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    boolean isUniqueUsername(String username);

    boolean isUniqueEmail(String email);

    UserResponse create(UserCreateRequest request);

    UserResponse findById(UUID id);

    UserResponse findByUsername(String username);

    List<UserResponse> findAll();

    boolean update(UUID srcUserId, UUID dstUserId, UserUpdateRequest request);

    boolean delete(UUID srcUserId, UUID dstUserId);
}