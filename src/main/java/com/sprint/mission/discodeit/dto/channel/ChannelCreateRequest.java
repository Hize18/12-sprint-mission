package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelCreateRequest(
        ChannelType channelType,
        UUID ownerId,
        String name
) {
    public ChannelCreateRequest {
        if (channelType == null) throw new IllegalArgumentException("channelType is null.");
        if (ownerId == null) throw new IllegalArgumentException("ownerId is null.");

        if (channelType == ChannelType.PUBLIC) {
            if (name == null) throw new IllegalArgumentException("name is null.");
            if (name.isBlank()) throw new IllegalArgumentException("name is blank.");
        }
    }
}
