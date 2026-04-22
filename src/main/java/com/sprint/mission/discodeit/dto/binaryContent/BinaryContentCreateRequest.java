package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] data
) {
    public BinaryContentCreateRequest {
        if (fileName == null) throw new IllegalArgumentException("fileName is null.");
        if (contentType == null) throw new IllegalArgumentException("contentType is null.");
        if (data == null) throw new IllegalArgumentException("data is null.");

        if (fileName.isBlank()) throw new IllegalArgumentException("fileName is blank.");
        if (contentType.isBlank()) throw new IllegalArgumentException("contentType is blank.");
        if (data.length == 0) throw new IllegalArgumentException("data is empty.");
    }
}
