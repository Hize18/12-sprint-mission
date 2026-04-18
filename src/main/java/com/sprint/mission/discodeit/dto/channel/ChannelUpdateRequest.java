package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record ChannelUpdateRequest(
        UUID channelId,
        String name
) {
    public ChannelUpdateRequest {
        if (channelId == null) throw new IllegalArgumentException("channelId is null.");
        if (name == null) throw new IllegalArgumentException("name is null");

        if (name.isBlank()) throw new IllegalArgumentException("name is blank.");
    }
}
