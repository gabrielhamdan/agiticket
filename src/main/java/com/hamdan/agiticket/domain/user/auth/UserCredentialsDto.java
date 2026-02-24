package com.hamdan.agiticket.domain.user.auth;

import jakarta.validation.constraints.NotBlank;

public record UserCredentialsDto(@NotBlank String userName, @NotBlank String password) {
}
