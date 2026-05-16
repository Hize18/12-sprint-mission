package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UnauthorizedException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional(readOnly = true)
  public UserDto login(LoginRequest loginRequest) {
    if (loginRequest == null) {
      throw new IllegalArgumentException("loginRequest is null.");
    }

    User loginUser = userRepository.findByUsername(loginRequest.username())
        .orElseThrow(() -> new UnauthorizedException("invalid username or password."));

    if (!Objects.equals(loginUser.getPassword(), loginRequest.password())) {
      throw new UnauthorizedException("invalid username or password.");
    }

    return userMapper.toDto(loginUser);
  }
}
