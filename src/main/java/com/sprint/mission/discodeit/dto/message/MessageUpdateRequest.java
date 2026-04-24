package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;

import java.util.List;

public record MessageUpdateRequest(
        String content,
        List<BinaryContentCreateRequest> attachmentList
) {
    public MessageUpdateRequest{
        if (content == null) throw new IllegalArgumentException("content is null.");
        if (content.isBlank()) throw new IllegalArgumentException("content is blank.");

        attachmentList = attachmentList == null ? List.of() : List.copyOf(attachmentList);
    }
}
