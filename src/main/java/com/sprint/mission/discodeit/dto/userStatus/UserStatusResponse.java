package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public record UserStatusResponse(
    UUID id,
    UUID userId,
    boolean online
) {

  public static UserStatusResponse from(UserStatus usrStatus) {
    if (usrStatus == null) {
      throw new IllegalArgumentException("userStatus is null.");
    }

    return new UserStatusResponse(
        usrStatus.getId(),
        usrStatus.getUserId(),
        usrStatus.isActive()
    );
  }

  @Override
  public String toString() {
    return "UserStatus [" +
        "userId = " + userId +
        ", online = " + online +
        "]";
  }
}
