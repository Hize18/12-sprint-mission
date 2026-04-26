package com.sprint.mission.discodeit.controller.REST;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<UserResponse> login(
      @RequestBody LoginRequest loginRequest,
      HttpSession session
  ) {
    UserResponse userResponse = authService.login(loginRequest);
    session.setAttribute("loginUser", userResponse);
    return ResponseEntity.ok(userResponse);
  }

  @RequestMapping(value = "/checkUser", method = RequestMethod.GET)
  public ResponseEntity<UserResponse> checkUser(
      HttpSession session
  ) {
    UserResponse userResponse = (UserResponse) session.getAttribute("loginUser");
    if (userResponse == null) {
      throw new IllegalArgumentException("not login yet.");
    }
    return ResponseEntity.ok(userResponse);
  }

  @RequestMapping(value = "/logout", method = RequestMethod.POST)
  public ResponseEntity<Void> logout(HttpSession session) {
    if (session.getAttribute("loginUser") == null) {
      throw new IllegalArgumentException("not login yet.");
    }
    session.invalidate();
    return ResponseEntity.noContent().build();
  }
}
