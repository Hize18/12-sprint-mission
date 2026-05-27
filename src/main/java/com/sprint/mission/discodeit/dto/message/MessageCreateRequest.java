package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
    UUID channelId,
    UUID authorId,
    String content,
    List<BinaryContentCreateRequest> attachmentList
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

    attachmentList = attachmentList == null ? List.of() : List.copyOf(attachmentList);
  }
}
