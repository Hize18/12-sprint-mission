package com.sprint.mission.discodeit.dto.channel;

public record ChannelUpdateRequest(
    String newName,
    String newDescription
) {

  public ChannelUpdateRequest {
    if (newName == null) {
      throw new IllegalArgumentException("newName is null");
    }

    if (newName.isBlank()) {
      throw new IllegalArgumentException("name is blank.");
    }
  }
}
