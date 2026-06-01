package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final UUID userId;
  private Instant lastActiveAt;
  private final Instant createdAt;
  private Instant updatedAt;

  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
  }

  public boolean isActive() {
    return !updatedAt.isBefore(Instant.now().minusSeconds(300));
  }

  public void update(Instant time) {
    if (time != null && !Objects.equals(lastActiveAt, time)) {
      lastActiveAt = time;
    }
    this.updatedAt = time;
  }
}
