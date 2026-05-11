package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    UUID profileId,
    String username,
    String email,
    Instant createdAt,
    Instant updatedAt,
    boolean online
) {

  public static UserDto from(User user, boolean online) {
    if (user == null) {
      throw new IllegalArgumentException("User is null");
    }

    return new UserDto(
        user.getId(),
        user.getProfileId(),
        user.getUsername(),
        user.getEmail(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        online
    );
  }
}
