package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channelId,
    UserDto author,
    List<BinaryContentDto> attachments
) {

  public MessageDto {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }

    if (content == null) {
      throw new IllegalArgumentException("content is null.");
    }
//    author은 nullable

    if (attachments == null) {
      throw new IllegalArgumentException("attachments is null.");
    }
  }
}
