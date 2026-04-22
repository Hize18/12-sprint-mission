package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;

public record UserCreateRequest(
        String username,
        String email,
        String password,
        BinaryContentCreateRequest profileImage
) {
    public UserCreateRequest {
        if (username == null) throw new IllegalArgumentException("username is null.");
        if (email == null) throw new IllegalArgumentException("email is null.");
        if (password == null) throw new IllegalArgumentException("password is null.");

        if (username.isBlank()) throw new IllegalArgumentException("username is blank.");
        if (email.isBlank()) throw new IllegalArgumentException("email is blank.");
        if (password.isBlank()) throw new IllegalArgumentException("password is blank.");
    }
}
