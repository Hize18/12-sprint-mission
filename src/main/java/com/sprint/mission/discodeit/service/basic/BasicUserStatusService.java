package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;

  @Override
  public UserStatus create(UserStatusCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    if (!userRepository.existsById(request.userId())) {
      throw new NoSuchElementException("user not found.");
    }

    if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
      throw new IllegalStateException("userStatus already exists.");
    }

    UserStatus usrStatus = new UserStatus(request.userId(), request.lastActiveAt());
    return userStatusRepository.save(usrStatus);
  }

  @Override
  public UserStatus findById(UUID userStatusId) {
    if (userStatusId == null) {
      throw new IllegalArgumentException("userStatusId is null.");
    }

    return userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));
  }

  @Override
  public UserStatus findByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus update(UUID userStatusId, UserStatusUpdateRequest request) {
    if (userStatusId == null) {
      throw new IllegalArgumentException("userStatusId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    UserStatus usrStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    usrStatus.update(request.newLastActiveAt());
    return userStatusRepository.save(usrStatus);
  }

  @Override
  public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    UserStatus usrStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    usrStatus.update(request.newLastActiveAt());
    return userStatusRepository.save(usrStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    if (userStatusId == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (!userStatusRepository.existsById(userStatusId)) {
      throw new NoSuchElementException("userStatus not found.");
    }
    userStatusRepository.delete(userStatusId);
  }
}
