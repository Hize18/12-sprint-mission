package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Channel implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private ChannelType channelType;
  private UUID ownerId;
  private String name;
  private final Instant createdAt;
  private Instant updatedAt;

  public Channel(UUID ownerId, String name, ChannelType channelType) {
    if (ownerId == null) {
      throw new IllegalArgumentException("ownerId is null.");
    }
    if (channelType == null) {
      throw new IllegalArgumentException("channelType is null.");
    }

    if (channelType == ChannelType.PUBLIC) {
      if (name == null) {
        throw new IllegalArgumentException("name is null.");
      }
      if (name.isBlank()) {
        throw new IllegalArgumentException("name is blank.");
      }
    }

    this.id = UUID.randomUUID();
    this.channelType = channelType;
    this.ownerId = ownerId;
    this.name = name;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public Channel(Channel channel) {
    if (channel == null) {
      throw new IllegalArgumentException("Channel is null.");
    }

    this.id = channel.getId();
    this.channelType = channel.getChannelType();
    this.ownerId = channel.getOwnerId();
    this.name = channel.getName();
    this.createdAt = channel.getCreatedAt();
    this.updatedAt = channel.getUpdatedAt();
  }

  public static Channel copyOf(Channel channel) {
    return new Channel(channel);
  }

  public void update(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name is null.");
    }
    if (name.isBlank()) {
      throw new IllegalArgumentException("name is blank.");
    }

    this.name = name;
    this.updatedAt = Instant.now();
  }
}
