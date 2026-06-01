package com.sprint.mission.discodeit.dto.user;

public record UserUpdateRequest(
    String newUsername,
    String newEmail,
    String newPassword
) {

  public UserUpdateRequest {
    if (newUsername != null && newUsername.isBlank()) {
      throw new IllegalArgumentException("newUsername is blank.");
    }
    if (newEmail != null && newEmail.isBlank()) {
      throw new IllegalArgumentException("newEmail is blank.");
    }
    if (newPassword != null && newPassword.isBlank()) {
      throw new IllegalArgumentException("newPassword is blank.");
    }
  }
}
