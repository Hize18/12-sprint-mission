package com.sprint.mission.discodeit.dto.channel;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    List<UUID> participantIds
) {

  public PrivateChannelCreateRequest {
    if (participantIds == null) {
      throw new IllegalArgumentException("participantIds is null.");
    }

    if (participantIds.isEmpty()) {
      throw new IllegalArgumentException("participantIds is empty.");
    }

    if (participantIds.stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("participantIds contains null.");
    }
  }
}
