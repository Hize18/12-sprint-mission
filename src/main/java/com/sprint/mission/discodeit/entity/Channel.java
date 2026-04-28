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
  private final ChannelType type;
  private String name;
  private String description;
  private final Instant createdAt;
  private Instant updatedAt;

  public Channel(String name, String description, ChannelType type) {
    if (type == null) {
      throw new IllegalArgumentException("type is null.");
    }

    if (type == ChannelType.PUBLIC) {
      if (name == null) {
        throw new IllegalArgumentException("name is null.");
      }
      if (description == null) {
        throw new IllegalArgumentException("description is null.");
      }
      if (name.isBlank()) {
        throw new IllegalArgumentException("name is blank.");
      }
    }

    this.id = UUID.randomUUID();
    this.type = type;
    this.name = name;
    this.description = description;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public Channel(Channel channel) {
    if (channel == null) {
      throw new IllegalArgumentException("Channel is null.");
    }

    this.id = channel.getId();
    this.type = channel.getType();
    this.name = channel.getName();
    this.description = channel.getDescription();
    this.createdAt = channel.getCreatedAt();
    this.updatedAt = channel.getUpdatedAt();
  }

  public static Channel copyOf(Channel channel) {
    return new Channel(channel);
  }

  public void update(String name, String description) {
    if (name == null) {
      throw new IllegalArgumentException("name is null.");
    }
    if (description == null) {
      throw new IllegalArgumentException("description is null.");
    }
    if (name.isBlank()) {
      throw new IllegalArgumentException("name is blank.");
    }

    this.name = name;
    this.description = description;
    this.updatedAt = Instant.now();
  }
}
