package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
    UUID id,
    ChannelType type,
    UUID ownerId,
    String name,
    Instant lastMessageAt,      // 추가
    List<UUID> participantIds
) {

  public static ChannelResponse from(Channel channel) {
    if (channel == null) {
      throw new IllegalArgumentException("Channel is null");
    }

    return from(channel, null, null);
  }

  public static ChannelResponse from(Channel channel, Instant lastMessageAt,
      List<UUID> participantIds) {
    if (channel == null) {
      throw new IllegalArgumentException("Channel is null");
    }

    return new ChannelResponse(
        channel.getId(),
        channel.getChannelType(),
        channel.getOwnerId(),
        channel.getName(),
        lastMessageAt,
        participantIds
    );
  }

  public static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
          .withZone(ZoneId.systemDefault());

  @Override
  public String toString() {
    return "Channel [" +
        "ownerId = " + ownerId +
        ", name = " + name +
        "]\t시간 = " +
        FORMATTER.format(lastMessageAt);
  }
}
