package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface UserService {

  UserDto create(UserCreateRequest request);

  UserDto findDetailById(UUID userId);

  List<UserDto> findAllWithFetch();

  UserDto update(UUID userId, UserUpdateRequest request);

  void delete(UUID userId);
}