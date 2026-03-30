package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Channel channel;
    private final User user;
    private String content;
    private final Long createdAt;
    private Long updatedAt;

    public Message(Channel channel, User user, String content) {
        if(channel == null || user == null || content == null) throw new IllegalArgumentException("Message is null.");

        this.id = UUID.randomUUID();
        this.channel = channel;
        this.user = user;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getUser() {return user;}

    public String getContent() {
        return content;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String content){
        if(content == null) throw new IllegalArgumentException("Message update error.");

        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "[" +
                "nickname = " + user.getNickname() +
                ", content = " + content +
                "]\t시간 = " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm")
                        .format(new Timestamp(updatedAt)) +
                (updatedAt > createdAt ? "(수정됨)":"");
    }
}
