package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final UUID channelId;
    private final UUID userId;
    private String content;
    private List<UUID> attachmentIds;
    private final Instant createdAt;
    private Instant updatedAt;

    public Message(UUID channelId, UUID userId, String content, List<UUID> attachmentIds) {
        if (channelId == null || userId == null || content == null || attachmentIds == null)
            throw new IllegalArgumentException("Message is null.");

        this.id = UUID.randomUUID();
        this.channelId = channelId;
        this.userId = userId;
        this.attachmentIds = List.copyOf(attachmentIds);
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public Message(Message message) {
        if (message == null) throw new IllegalArgumentException("Message is null.");

        this.id = message.getId();
        this.channelId = message.getChannelId();
        this.userId = message.getUserId();
        this.attachmentIds = List.copyOf(message.getAttachmentIds());
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }

    public static Message copyOf(Message message){
        return new Message(message);
    }

    public void update(String content, List<UUID> attachmentIds) {
        if (content == null || attachmentIds == null)
            throw new IllegalArgumentException("Message update error.");

        this.attachmentIds = List.copyOf(attachmentIds);
        this.content = content;
        this.updatedAt = Instant.now();
    }
}
