package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;

  @Override
  public User login(LoginRequest loginRequest) {
    if (loginRequest == null) {
      throw new IllegalArgumentException("loginRequest is null.");
    }

    User loginUser = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(() -> new IllegalStateException("invalid username or password."));

    if (!Objects.equals(loginUser.getPassword(), loginRequest.password())) {
      throw new IllegalStateException("invalid username or password.");
    }

    return loginUser;
  }
}
