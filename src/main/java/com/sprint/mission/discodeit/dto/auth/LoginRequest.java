package com.sprint.mission.discodeit.dto.auth;

public record LoginRequest(
    String username,
    String password
) {

  public LoginRequest {
    if (username == null) {
      throw new IllegalArgumentException("username is null.");
    }
    if (username.isBlank()) {
      throw new IllegalArgumentException("username is blank.");
    }

    if (password == null) {
      throw new IllegalArgumentException("password is null.");
    }
    if (password.isBlank()) {
      throw new IllegalArgumentException("password is blank.");
    }
  }
}
