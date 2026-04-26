package com.sprint.mission.discodeit.dto.readStatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record ReadStatusResponse(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant updatedAt
) {

  public static ReadStatusResponse from(ReadStatus status) {
    if (status == null) {
      throw new IllegalArgumentException("readStatus is null.");
    }

    return new ReadStatusResponse(
        status.getId(),
        status.getUserId(),
        status.getChannelId(),
        status.getUpdatedAt()
    );
  }

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
          .withZone(ZoneId.systemDefault());

  @Override
  public String toString() {
    return "ReadStatus [" +
        "userId = " + userId +
        ", channelId = " + channelId +
        "]\t마지막 읽은 시간 = " +
        FORMATTER.format(updatedAt);
  }
}
