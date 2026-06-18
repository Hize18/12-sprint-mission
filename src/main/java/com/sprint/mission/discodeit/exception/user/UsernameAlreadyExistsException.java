package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UsernameAlreadyExistsException extends UserException {

  public UsernameAlreadyExistsException() {
    super(ErrorCode.USERNAME_DUPLICATED);
  }

  public static UsernameAlreadyExistsException withUsername(String username) {
    UsernameAlreadyExistsException ex = new UsernameAlreadyExistsException();
    ex.addDetail("username", username);
    return ex;
  }
}
