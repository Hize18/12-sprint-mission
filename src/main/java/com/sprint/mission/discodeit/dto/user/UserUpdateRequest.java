package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(max = 100)
    String newUsername,

    @Email
    @Size(max = 100)
    String newEmail,

    @Size(min = 8, max = 60, message = "비밀번호는 8자 이상 60자 이하여야 합니다")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,}$",
        message = "비밀번호는 숫자, 문자, 특수문자를 포함해야 합니다"
    )
    String newPassword
) {

  //null로 받은 경우 - 수정 x, notnull로 받은 경우 수정.
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
