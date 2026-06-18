package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "Username 필수.")
    @Size(min = 1, max = 100)
    String username,

    @NotBlank(message = "password 필수.")
    @Size(min = 1, max = 100)
    String password
) {

}
