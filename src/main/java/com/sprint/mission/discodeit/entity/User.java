package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private final UUID id;
  private UUID profileId;
  private String username;
  private String email;
  private String password;
  private final Instant createdAt;
  private Instant updatedAt;

  public User(String username, String email, String password, UUID profileId) {
    if (username == null) {
      throw new IllegalArgumentException("username is null.");
    }
    if (email == null) {
      throw new IllegalArgumentException("email is null.");
    }
    if (password == null) {
      throw new IllegalArgumentException("password is null.");
    }

    if (username.isBlank()) {
      throw new IllegalArgumentException("username is blank.");
    }
    if (email.isBlank()) {
      throw new IllegalArgumentException("email is blank.");
    }
    if (password.isBlank()) {
      throw new IllegalArgumentException("password is blank.");
    }

    id = UUID.randomUUID();
    this.profileId = profileId;
    this.username = username;
    this.email = email;
    this.password = password;
    createdAt = Instant.now();
    updatedAt = createdAt;
  }

  public User(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User is null.");
    }

    this.id = user.getId();
    this.profileId = user.getProfileId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
  }

  public static User copyOf(User user) {
    return new User(user);
  }

  public void update(String username, String email, String password, UUID profileId) {
    if (username == null) {
      throw new IllegalArgumentException("username is null.");
    }
    if (email == null) {
      throw new IllegalArgumentException("email is null.");
    }
    if (password == null) {
      throw new IllegalArgumentException("password is null.");
    }

    if (username.isBlank()) {
      throw new IllegalArgumentException("username is blank.");
    }
    if (email.isBlank()) {
      throw new IllegalArgumentException("email is blank.");
    }
    if (password.isBlank()) {
      throw new IllegalArgumentException("password is blank.");
    }

    this.profileId = profileId;
    this.username = username;
    this.email = email;
    this.password = password;
    updatedAt = Instant.now();
  }
}