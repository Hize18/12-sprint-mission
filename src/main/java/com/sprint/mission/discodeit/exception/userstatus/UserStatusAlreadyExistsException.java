package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {

  public UserStatusAlreadyExistsException() {
    super(ErrorCode.USER_STATUS_DUPLICATED);
  }

  public static UserStatusAlreadyExistsException withUserId(UUID userId) {
    UserStatusAlreadyExistsException ex = new UserStatusAlreadyExistsException();
    ex.addDetail("userId", userId);
    return ex;
  }
}
