package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    Instant lastMessageAt,
    List<UserDto> participants
) {

  public ChannelDto {
    if (id == null) {
      throw new IllegalArgumentException("id is null.");
    }

    if (type == null) {
      throw new IllegalArgumentException("type is null.");
    }

    if (type == ChannelType.PUBLIC) {

      if (name == null || name.isBlank()) {
        throw new IllegalArgumentException("name is blank.");
      }
      if (description == null) {
        throw new IllegalArgumentException("description is null.");
      }
    }

    participants = participants == null ? List.of() : List.copyOf(participants);
  }
}
