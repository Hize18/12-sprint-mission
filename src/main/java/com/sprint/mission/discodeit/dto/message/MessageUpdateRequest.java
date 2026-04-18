package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;

import java.util.List;
import java.util.UUID;

public record MessageUpdateRequest(
        UUID messageId,
        String content,
        List<BinaryContentCreateRequest> attachmentList
) {
    public MessageUpdateRequest{
        if (messageId == null) throw new IllegalArgumentException("messageId is null.");
        if (content == null) throw new IllegalArgumentException("content is null.");
        if (content.isBlank()) throw new IllegalArgumentException("content is blank.");

        attachmentList = attachmentList == null ? List.of() : List.copyOf(attachmentList);
    }
}
