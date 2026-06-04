package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class EmailAlreadyExistsException extends UserException {

  public EmailAlreadyExistsException() {
    super(ErrorCode.EMAIL_DUPLICATED);
  }

  public static EmailAlreadyExistsException withEmail(String email) {
    EmailAlreadyExistsException ex = new EmailAlreadyExistsException();
    ex.addDetail("email", email);
    return ex;
  }
}
