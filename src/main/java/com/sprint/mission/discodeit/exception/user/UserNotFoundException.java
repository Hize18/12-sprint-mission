package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.List;
import java.util.UUID;

public class UserNotFoundException extends UserException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }

  public static UserNotFoundException withUsername(String username) {
    UserNotFoundException ex = new UserNotFoundException();
    ex.addDetail("username", username);
    return ex;
  }

  public static UserNotFoundException withUserId(UUID userId) {
    UserNotFoundException ex = new UserNotFoundException();
    ex.addDetail("userId", userId);
    return ex;
  }

  public static UserNotFoundException withUserIds(List<UUID> userIds) {
    UserNotFoundException ex = new UserNotFoundException();
    ex.addDetail("userId", userIds);
    return ex;
  }

}
