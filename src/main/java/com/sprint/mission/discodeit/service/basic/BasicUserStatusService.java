package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  @Transactional
  public UserStatusDto create(UserStatusCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withUserId(request.userId()));

    if (userStatusRepository.existsByUserId(user.getId())) {
      throw UserStatusAlreadyExistsException.withUserId(user.getId());
    }

    UserStatus usrStatus = new UserStatus(user, request.lastActiveAt());
    return userStatusMapper.toDto(userStatusRepository.save(usrStatus));
  }

  @Override
  @Transactional(readOnly = true)
  public UserStatusDto findById(UUID userStatusId) {
    if (userStatusId == null) {
      throw new IllegalArgumentException("userStatusId is null.");
    }

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> UserStatusNotFoundException.withUserStatusId(userStatusId));

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    if (userStatusId == null) {
      throw new IllegalArgumentException("userStatusId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> UserStatusNotFoundException.withUserStatusId(userStatusId));

    userStatus.setLastActiveAt(request.newLastActiveAt());
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> UserStatusNotFoundException.withUserStatusId(userId));

    userStatus.setLastActiveAt(request.newLastActiveAt());
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public void delete(UUID userStatusId) {
    if (userStatusId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> UserStatusNotFoundException.withUserStatusId(userStatusId));

    userStatusRepository.delete(userStatus);
  }
}
