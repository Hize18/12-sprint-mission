package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponse;
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
  public UserStatusResponse create(UserStatusCreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    if (!userRepository.existsById(request.userId())) {
      throw new NoSuchElementException("user not found.");
    }

    if (userStatusRepository.findByUserId(request.userId()).isPresent()) {
      throw new IllegalStateException("userStatus already exists.");
    }

    UserStatus usrStatus = new UserStatus(request.userId());
    userStatusRepository.save(usrStatus);
    return UserStatusResponse.from(usrStatus);
  }

  @Override
  public UserStatusResponse findById(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return UserStatusResponse.from(
        userStatusRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("userStatus not found."))
    );
  }

  @Override
  public UserStatusResponse findByUserId(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    return UserStatusResponse.from(
        userStatusRepository.findByUserId(id)
            .orElseThrow(() -> new NoSuchElementException("userStatus not found."))
    );
  }

  @Override
  public List<UserStatusResponse> findAll() {
    return userStatusRepository.findAll().stream()
        .map(UserStatusResponse::from)
        .toList();
  }

  @Override
  public UserStatusResponse update(UserStatusUpdateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("request is null.");
    }

    UserStatus usrStatus = userStatusRepository.findById(request.id())
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    usrStatus.update();
    return UserStatusResponse.from(
        userStatusRepository.save(usrStatus)
    );
  }

  @Override
  public UserStatusResponse updateByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    UserStatus usrStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    usrStatus.update();
    return UserStatusResponse.from(
        userStatusRepository.save(usrStatus)
    );
  }

  @Override
  public void delete(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (!userStatusRepository.existsById(id)) {
      throw new NoSuchElementException("userStatus not found.");
    }
    userStatusRepository.delete(id);
  }
}
