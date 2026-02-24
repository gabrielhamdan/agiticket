package com.hamdan.agiticket.domain.user;

import com.hamdan.agiticket.api.security.Password;
import com.hamdan.agiticket.domain.user.permission.EUserRole;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDataDto(
        @NotNull Long id,
        String password,
        @Password String newPassword,
        EUserRole role
) {
}
