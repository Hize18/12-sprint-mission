package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ReadStatus implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final UUID userId;
  private final UUID channelId;
  private Instant lastReadAt;
  private final Instant createdAt;
  private Instant updatedAt;

  public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
    if (userId == null) {
      throw new IllegalArgumentException("userId is null.");
    }
    if (channelId == null) {
      throw new IllegalArgumentException("channelId is null.");
    }
    if (lastReadAt == null) {
      throw new IllegalArgumentException("lastReadAt is null.");
    }

    this.id = UUID.randomUUID();
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = lastReadAt;
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
  }

  public void updateTime(Instant time) {
    if (time != null && !Objects.equals(lastReadAt, time)) {
      lastReadAt = time;
    }
    this.updatedAt = time;
  }

}
