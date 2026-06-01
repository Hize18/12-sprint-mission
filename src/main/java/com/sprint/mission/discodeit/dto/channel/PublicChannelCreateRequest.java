package com.sprint.mission.discodeit.dto.channel;

public record PublicChannelCreateRequest(
    String name,
    String description
) {

  public PublicChannelCreateRequest {
    if (name == null) {
      throw new IllegalArgumentException("name is null.");
    }
    if (description == null) {
      throw new IllegalArgumentException("description is null.");
    }
    if (name.isBlank()) {
      throw new IllegalArgumentException("name is blank.");
    }
  }
}
