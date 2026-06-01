package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final UUID channelId;
  private final UUID authorId;
  private String content;
  private List<UUID> attachmentIds;
  private final Instant createdAt;
  private Instant updatedAt;

  public Message(UUID channelId, UUID authorId, String content, List<UUID> attachmentIds) {
    if (channelId == null || authorId == null || content == null || attachmentIds == null) {
      throw new IllegalArgumentException("Message is null.");
    }

    this.id = UUID.randomUUID();
    this.channelId = channelId;
    this.authorId = authorId;
    this.attachmentIds = List.copyOf(attachmentIds);
    this.content = content;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public Message(Message message) {
    if (message == null) {
      throw new IllegalArgumentException("Message is null.");
    }

    this.id = message.getId();
    this.channelId = message.getChannelId();
    this.authorId = message.getAuthorId();
    this.attachmentIds = List.copyOf(message.getAttachmentIds());
    this.content = message.getContent();
    this.createdAt = message.getCreatedAt();
    this.updatedAt = message.getUpdatedAt();
  }

  public static Message copyOf(Message message) {
    return new Message(message);
  }

  public void update(String content, List<UUID> attachmentIds) {
    if (content == null || attachmentIds == null) {
      throw new IllegalArgumentException("Message update error.");
    }

    this.attachmentIds = List.copyOf(attachmentIds);
    this.content = content;
    this.updatedAt = Instant.now();
  }
}
