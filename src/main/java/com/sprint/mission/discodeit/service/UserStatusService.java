package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import java.util.UUID;

public interface UserStatusService {

  UserStatusDto create(UserStatusCreateRequest request);

  UserStatusDto findById(UUID userStatusId);

  UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request);

  UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request);

  void delete(UUID userStatusId);
}
