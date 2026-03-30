package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private String username;
    private String email;
    private String password;
    private String nickname;
    private final Long createdAt;
    private Long updatedAt;

    public User(String username, String email, String password, String nickname) {
        if(username == null || email == null || password == null || nickname == null) throw new IllegalArgumentException("User is null.");

        id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        createdAt = System.currentTimeMillis();
        updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String username, String email, String password, String nickname){
        if(username == null || email == null || password == null || nickname == null) throw new IllegalArgumentException("User update error.");

        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "User [" +
                "username = " + username +
                ", email = " + email +
                ", password = " + password +
                ", nickname = " + nickname +
                "] 시간 = " + new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(new Timestamp(updatedAt)) +
                (updatedAt > createdAt ? "(수정됨)":"");
    }
}