package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageCreateRequest(
    UUID channelId,
    UUID authorId,
    String content
) {

  public MessageCreateRequest {
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }
    if (authorId == null) {
      throw new IllegalArgumentException("authorId is null.");
    }

    if (content == null) {
      throw new IllegalArgumentException("content is null.");
    }
  }
}
