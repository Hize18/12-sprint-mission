package com.sprint.mission.discodeit.dto.channel;

public record ChannelUpdateRequest(
    String newName,
    String newDescription
) {

  public ChannelUpdateRequest {
    if (newName == null) {
      throw new IllegalArgumentException("newName is null.");
    }

    if (newDescription == null) {
      throw new IllegalArgumentException("newDescription is null.");
    }
  }
}
