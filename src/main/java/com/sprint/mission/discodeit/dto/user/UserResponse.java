package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record UserResponse(
        UUID id,
        UUID profileId,
        String username,
        String email,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse from(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }

        return new UserResponse(
                user.getId(),
                user.getProfileId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    .withZone(ZoneId.systemDefault());

    @Override
    public String toString() {
        return "User [" +
                "username = " + username +
                ", email = " + email +
                "] 시간 = " +
                FORMATTER.format(updatedAt)
                + (updatedAt.isAfter(createdAt) ? "(수정됨)" : "");
    }
}
