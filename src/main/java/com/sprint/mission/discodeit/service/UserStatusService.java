package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponse create(UserStatusCreateRequest request);

    UserStatusResponse findById(UUID id);

    UserStatusResponse findByUserId(UUID id);

    List<UserStatusResponse> findAll();

    void update(UserStatusUpdateRequest request);

    void updateByUserId(UUID UserId);

    void delete(UUID id);
}
