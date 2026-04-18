package com.sprint.mission.discodeit.dto.userStatus;

import java.util.UUID;

public record UserStatusUpdate(
        UUID id
) {
    public UserStatusUpdate {
        if (id == null) throw new IllegalArgumentException("id is null.");
    }
}
