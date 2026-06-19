package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.InvalidLoginCredentialsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        .orElseThrow(() -> {
          log.info("로그인 실패 : username ={}", loginRequest.username());
          return new InvalidLoginCredentialsException();
        });

    if (!Objects.equals(loginUser.getPassword(), loginRequest.password())) {
      log.info("로그인 실패 : username ={}", loginRequest.username());
      throw new InvalidLoginCredentialsException();
    }

    log.info("로그인 성공 : username ={}", loginRequest.username());
    return userMapper.toDto(loginUser);
  }
}
