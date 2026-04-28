package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    Instant lastMessageAt,      // 추가
    List<UUID> participantIds
) {

  public static ChannelDto from(Channel channel) {
    if (channel == null) {
      throw new IllegalArgumentException("Channel is null");
    }

    return from(channel, null, null);
  }

  public static ChannelDto from(Channel channel, Instant lastMessageAt,
      List<UUID> participantIds) {
    if (channel == null) {
      throw new IllegalArgumentException("Channel is null");
    }

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        "NEED_TO_IMPLEMENT_DESCRIPTION_PART",
        lastMessageAt,
        participantIds
    );
  }
}
