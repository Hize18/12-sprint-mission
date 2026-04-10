package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private String username;
    private String email;
    private String password;
    private final Instant createdAt;
    private Instant updatedAt;

    public User(String username, String email, String password) {
        if (username == null || email == null || password == null)
            throw new IllegalArgumentException("User is null.");

        id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    public User(User user) {
        if (user == null) throw new IllegalArgumentException("User is null.");

        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public static User copyOf(User user){
        return new User(user);
    }

    public void update(String username, String email, String password) {
        if (username == null || email == null || password == null)
            throw new IllegalArgumentException("User update error.");

        this.username = username;
        this.email = email;
        this.password = password;
        updatedAt = Instant.now();
    }

    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault());

    @Override
    public String toString() {
        return "User [" +
                "username = " + username +
                ", email = " + email +
                ", password = " + password +
                "] 시간 = " +
                FORMATTER.format(updatedAt)
                + (updatedAt.isAfter(createdAt) ? "(수정됨)" : "");
    }
}