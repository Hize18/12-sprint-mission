package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private User owner;
    private String name;
    private String handle;
    private final Long createdAt;
    private Long updatedAt;

    public Channel(User owner, String name, String handle) {
        if(owner == null || name == null || handle == null) throw new IllegalArgumentException("Channel is null.");

        this.id = UUID.randomUUID();
        this.owner = owner;
        this.name = name;
        this.handle = handle;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getHandle() {
        return handle;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String name, String handle){
        if(name == null || handle == null) throw new IllegalArgumentException("Channel update error.");

        this.name = name;
        this.handle = handle;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Channel [" +
                "owner = " + owner.getNickname() +
                ", name = " + name +
                ", handle = " + handle +
                "]\t시간 = " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm")
                        .format(new Timestamp(updatedAt)) +
                (updatedAt > createdAt ? "(수정됨)":"");
    }
}
