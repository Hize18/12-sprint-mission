package com.sprint.mission.discodeit.dto.userStatus;

import java.util.UUID;

public record UserStatusCreateRequest(
        UUID userId
) {
    public UserStatusCreateRequest {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
    }
}
