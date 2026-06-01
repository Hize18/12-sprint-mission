package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
) {

  public UserDto {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }
    if (username == null || username.isBlank()) {
      throw new IllegalArgumentException("username is blank.");
    }
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("email is blank.");
    }
    if (online == null) {
      throw new IllegalArgumentException("online is null.");
    }
  }
}
