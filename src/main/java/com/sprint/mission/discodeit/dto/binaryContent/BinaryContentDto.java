package com.sprint.mission.discodeit.dto.binaryContent;

import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    long size,
    String contentType
) {

  public BinaryContentDto {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (fileName == null || fileName.isBlank()) {
      throw new IllegalArgumentException("fileName is blank.");
    }

    if (size < 0) {
      throw new IllegalArgumentException("size is negative.");
    }

    if (contentType == null || contentType.isBlank()) {
      throw new IllegalArgumentException("contentType is blank.");
    }
  }

}
