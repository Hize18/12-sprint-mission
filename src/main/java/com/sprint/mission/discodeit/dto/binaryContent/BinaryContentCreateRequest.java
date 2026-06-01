package com.sprint.mission.discodeit.dto.binaryContent;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public BinaryContentCreateRequest {
    if (fileName == null || fileName.isBlank()) {
      throw new IllegalArgumentException("fileName is blank.");
    }
    if (contentType == null || contentType.isBlank()) {
      throw new IllegalArgumentException("contentType is blank.");
    }
    if (bytes == null || bytes.length == 0) {
      throw new IllegalArgumentException("data is empty.");
    }
  }
}
