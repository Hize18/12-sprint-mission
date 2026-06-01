package com.sprint.mission.discodeit.dto.message;

public record MessageUpdateRequest(
    String newContent
) {

  public MessageUpdateRequest {
    if (newContent == null) {
      throw new IllegalArgumentException("newContent is null.");
    }
  }
}
