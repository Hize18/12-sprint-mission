package com.sprint.mission.discodeit.dto.channel;

public record ChannelUpdateRequest(
        String name
) {
    public ChannelUpdateRequest {
        if (name == null) throw new IllegalArgumentException("name is null");

        if (name.isBlank()) throw new IllegalArgumentException("name is blank.");
    }
}
