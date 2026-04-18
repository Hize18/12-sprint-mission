package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository ur;

    @Override
    public UserResponse login(LoginRequest loginRequest) {
        if(loginRequest == null) throw new IllegalArgumentException("loginRequest is null.");

        User loginUser = ur.findByUsername(loginRequest.username())
                .orElseThrow(() -> new IllegalStateException("invalid username or password."));

        if(!Objects.equals(loginUser.getPassword(), loginRequest.password()))
            throw new IllegalStateException("invalid username or password.");

        return UserResponse.from(loginUser);
    }
}
