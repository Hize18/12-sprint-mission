package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        UUID channelId,
        UUID userId,
        String content,
        List<BinaryContentCreateRequest> attachmentList
) {
    public MessageCreateRequest {
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");
        if (userId == null) throw new IllegalArgumentException("userId is null.");

        if (content == null) throw new IllegalArgumentException("content is null.");
        if (content.isBlank()) throw new IllegalArgumentException("content is blank.");

        attachmentList = attachmentList == null ? List.of() : List.copyOf(attachmentList);
    }
}
