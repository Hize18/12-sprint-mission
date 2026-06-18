package com.sprint.mission.discodeit.exception.auth;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class InvalidLoginCredentialsException extends AuthException {

  public InvalidLoginCredentialsException() {
    super(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
  }
}
