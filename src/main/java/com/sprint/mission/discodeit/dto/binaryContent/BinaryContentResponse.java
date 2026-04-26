package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    String fileName,
    String contentType,
    byte[] bytes,
    Instant createdAt
) {

  public static BinaryContentResponse from(BinaryContent bc) {
    if (bc == null) {
      throw new IllegalArgumentException("binaryContent is null.");
    }

    return new BinaryContentResponse(
        bc.getId(),
        bc.getFileName(),
        bc.getContentType(),
        Arrays.copyOf(bc.getBytes(), bc.getBytes().length),
        bc.getCreatedAt()
    );
  }

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
          .withZone(ZoneId.systemDefault());

  @Override
  public String toString() {
    return "BinaryContent [" +
        "fileName = " + fileName +
        ", contentType = " + contentType +
        "]\t시간 = " +
        FORMATTER.format(createdAt);
  }
}
