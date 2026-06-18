package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateRequest(
    @NotNull UUID channelId,

    @NotNull UUID authorId,

    String content//프론트에서 첨부파일 있을경우 blank가 패스됨.
) {

}
