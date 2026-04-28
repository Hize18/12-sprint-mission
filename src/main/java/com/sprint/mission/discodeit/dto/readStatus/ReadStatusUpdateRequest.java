package com.sprint.mission.discodeit.dto.readStatus;

import java.time.Instant;

public record ReadStatusUpdateRequest(
    Instant newLastReadAt
) {

  public ReadStatusUpdateRequest {
    if (newLastReadAt == null) {
      throw new IllegalArgumentException("newLastReadAt is null.");
    }
  }
}
