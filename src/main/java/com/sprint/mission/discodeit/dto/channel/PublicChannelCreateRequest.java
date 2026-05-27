package com.sprint.mission.discodeit.dto.channel;

public record PublicChannelCreateRequest(
    String name,
    String description
) {

  public PublicChannelCreateRequest {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("name is blank.");
    }
    if (description == null) {
      throw new IllegalArgumentException("description is null.");
    }
  }
}
