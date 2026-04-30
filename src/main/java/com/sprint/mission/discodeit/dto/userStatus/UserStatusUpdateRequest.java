package com.sprint.mission.discodeit.dto.userStatus;

import java.util.UUID;

public record UserStatusUpdateRequest(
        UUID id
) {
    public UserStatusUpdateRequest {
        if (id == null) throw new IllegalArgumentException("id is null.");
    }
}
