package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

  public ReadStatusCreateRequest {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }
    if (lastReadAt == null) {
      throw new IllegalArgumentException("lasReadAt is null.");
    }
  }
}
