package com.sprint.mission.discodeit.dto.auth;

public record LoginRequest(
    String username,
    String password
) {

  public LoginRequest {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("username is blank.");
    }

    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("password is blank.");
    }
  }
}
