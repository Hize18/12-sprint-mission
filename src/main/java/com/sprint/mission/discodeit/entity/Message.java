package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final UUID channelId;
    private final UUID userId;
    private String content;
    private final Instant createdAt;
    private Instant updatedAt;

    public Message(UUID channelId, UUID userId, String content) {
        if (channelId == null || userId == null || content == null) throw new IllegalArgumentException("Message is null.");

        this.id = UUID.randomUUID();
        this.channelId = channelId;
        this.userId = userId;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public Message(Message message) {
        if (message == null) throw new IllegalArgumentException("Message is null.");

        this.id = message.getId();
        this.channelId = message.getChannelId();
        this.userId = message.getUserId();
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
    }

    public static Message copyOf(Message message){
        return new Message(message);
    }

    public void update(String content) {
        if (content == null) throw new IllegalArgumentException("Message update error.");

        this.content = content;
        this.updatedAt = Instant.now();
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault());

    @Override
    public String toString() {
        return "[" +
                "channelId = " + channelId +
                ", userId = " + userId +
                ", content = " + content +
                "]\t시간 = " +
                FORMATTER.format(updatedAt)
                + (updatedAt.isAfter(createdAt) ? "(수정됨)" : "");
    }
}
