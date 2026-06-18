package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank
    @Size(max = 100)
    String name,

    @NotNull
    String description
) {

}
