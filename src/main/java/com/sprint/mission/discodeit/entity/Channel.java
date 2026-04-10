package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private UUID ownerId;
    private String name;
    private final Instant createdAt;
    private Instant updatedAt;

    public Channel(User owner, String name) {
        if (owner == null || name == null) throw new IllegalArgumentException("Channel is null.");

        this.id = UUID.randomUUID();
        this.ownerId = owner.getId();
        this.name = name;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public Channel(Channel channel) {
        if (channel == null) throw new IllegalArgumentException("Channel is null.");

        this.id = channel.getId();
        this.ownerId = channel.getOwnerId();
        this.name = channel.getName();
        this.createdAt = channel.getCreatedAt();
        this.updatedAt = channel.getUpdatedAt();
    }

    public static Channel copyOf(Channel channel){
        return new Channel(channel);
    }

    public void update(String name) {
        if (name == null) throw new IllegalArgumentException("Channel update error.");

        this.name = name;
        this.updatedAt = Instant.now();
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault());

    @Override
    public String toString() {
        return "Channel [" +
                "ownerId = " + ownerId +
                ", name = " + name +
                "]\t시간 = " +
                FORMATTER.format(updatedAt)
                + (updatedAt.isAfter(createdAt) ? "(수정됨)" : "");
    }
}
