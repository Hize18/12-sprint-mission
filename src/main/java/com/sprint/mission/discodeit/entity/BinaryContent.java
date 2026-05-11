package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BinaryContent implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final String fileName;
  private final String contentType;
  private final long size;
  private final byte[] bytes;
  private final Instant createdAt;

  public BinaryContent(String fileName, String contentType, byte[] bytes) {
    if (fileName == null) {
      throw new IllegalArgumentException("fileName is null.");
    }
    if (contentType == null) {
      throw new IllegalArgumentException("contentType is null.");
    }
    if (bytes == null) {
      throw new IllegalArgumentException("content is null.");
    }

    if (fileName.isBlank()) {
      throw new IllegalArgumentException("fileName is blank.");
    }
    if (contentType.isBlank()) {
      throw new IllegalArgumentException("contentType is blank.");
    }
    if (bytes.length == 0) {
      throw new IllegalArgumentException("content is empty.");
    }

    this.id = UUID.randomUUID();
    this.fileName = fileName;
    this.contentType = contentType;
    this.size = bytes.length;
    this.bytes = Arrays.copyOf(bytes, bytes.length);
    this.createdAt = Instant.now();
  }
}
