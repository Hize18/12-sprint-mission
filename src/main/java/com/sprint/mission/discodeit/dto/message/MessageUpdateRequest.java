package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import java.util.List;

public record MessageUpdateRequest(
    String newContent,
    List<BinaryContentCreateRequest> attachmentList
) {

  public MessageUpdateRequest {
    if (newContent == null) {
      throw new IllegalArgumentException("newContent is null.");
    }

    attachmentList = attachmentList == null ? List.of() : List.copyOf(attachmentList);
  }
}
