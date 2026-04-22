package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdate;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatusResponse create(UserStatusCreate request);

    UserStatusResponse findById(UUID id);

    List<UserStatusResponse> findAll();

    boolean update(UserStatusUpdate request);

    boolean updateByUserId(UUID UserId);

    boolean delete(UUID id);
}
