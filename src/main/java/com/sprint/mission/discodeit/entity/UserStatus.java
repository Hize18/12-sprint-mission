package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user_statuses")
@Getter
@Setter
@ToString(callSuper = true, exclude = "user")
@SuperBuilder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true, nullable = false)
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant lastActiveAt;

  public UserStatus(User user, Instant lastActiveAt) {
    if (user == null) {
      throw new IllegalArgumentException("user is null.");
    }
    if (lastActiveAt == null) {
      throw new IllegalArgumentException("lastActiveAt is null.");
    }

    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }

  public boolean isOnline() {
    return !lastActiveAt.isBefore(Instant.now().minusSeconds(300));
  }
}
