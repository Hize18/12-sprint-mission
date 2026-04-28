package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

  public UserStatusUpdateRequest {
    if (newLastActiveAt == null) {
      throw new IllegalArgumentException("newLastActiveAt is null.");
    }
  }
}
