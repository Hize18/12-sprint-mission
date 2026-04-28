package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword,
    BinaryContentCreateRequest profileImage
) {

  public UserUpdateRequest {
    if (newUsername != null && newUsername.isBlank()) {
      throw new IllegalArgumentException("newUsername is blank.");
    }
    if (newEmail != null && newEmail.isBlank()) {
      throw new IllegalArgumentException("newEmail is blank.");
    }
  }
}
