package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;

public record UserCreateRequest(
    String username,
    String email,
    String password,
    BinaryContentCreateRequest profileImage
) {

  public UserCreateRequest {
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("username is blank.");
    }
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("email is blank.");
    }
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("password is blank.");
    }
  }
}
