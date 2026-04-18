package com.sprint.mission.discodeit.dto.userStatus;

import java.util.UUID;

public record UserStatusCreate(
        UUID userId
) {
    public UserStatusCreate {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
    }
}
