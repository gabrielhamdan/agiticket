package com.hamdan.agiticket.domain.user;

import com.hamdan.agiticket.api.security.Password;
import com.hamdan.agiticket.domain.user.permission.EUserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewUserDto(
        @NotBlank String userName,
        @NotBlank @Password String password,
        @NotNull EUserRole role
    ) {
}
