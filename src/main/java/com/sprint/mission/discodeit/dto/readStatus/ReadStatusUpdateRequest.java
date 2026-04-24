package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record ReadStatusUpdateRequest(
        UUID userId,
        UUID channelId
) {
    public ReadStatusUpdateRequest {
        if (userId == null) throw new IllegalArgumentException("userId is null.");
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");
    }
}
