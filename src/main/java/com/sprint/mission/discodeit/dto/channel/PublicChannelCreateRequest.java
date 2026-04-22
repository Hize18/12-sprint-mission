package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record PublicChannelCreateRequest(
        UUID ownerId,
        String name
) {
    public PublicChannelCreateRequest {
        if (ownerId == null) throw new IllegalArgumentException("ownerId is null.");
        if (name == null) throw new IllegalArgumentException("name is null.");
        if (name.isBlank()) throw new IllegalArgumentException("name is blank.");
    }
}
