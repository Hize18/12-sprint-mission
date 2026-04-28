package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    UUID userId,
    Instant lastActiveAt
) {

  public UserStatusCreateRequest {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (lastActiveAt == null) {
      throw new IllegalArgumentException("lastActiveAt is null.");
    }
  }
}
