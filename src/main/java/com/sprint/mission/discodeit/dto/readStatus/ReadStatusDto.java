package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public ReadStatusDto {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }

    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }
    if (lastReadAt == null) {
      throw new IllegalArgumentException("lastReadAt is null.");
    }
  }
}
