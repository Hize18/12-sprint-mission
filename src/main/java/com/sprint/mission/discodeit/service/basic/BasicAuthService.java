package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserResponse login(LoginRequest loginRequest) {
    if (loginRequest == null) {
      throw new IllegalArgumentException("loginRequest is null.");
    }

    User loginUser = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(() -> new IllegalStateException("invalid username or password."));

    if (!Objects.equals(loginUser.getPassword(), loginRequest.password())) {
      throw new IllegalStateException("invalid username or password.");
    }

    return toResponse(loginUser);
  }

  private UserResponse toResponse(User user) {
    boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isActive)
        .orElseThrow(() -> new NoSuchElementException("userStatus not found."));

    return UserResponse.from(user, online);
  }
}
