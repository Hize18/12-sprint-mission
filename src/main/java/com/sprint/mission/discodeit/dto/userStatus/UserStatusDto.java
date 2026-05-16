package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

  public UserStatusDto {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (lastActiveAt == null) {
      throw new IllegalArgumentException("lastActiveAt is null.");
    }
  }
}
