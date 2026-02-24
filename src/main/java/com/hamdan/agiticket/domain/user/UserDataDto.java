package com.hamdan.agiticket.domain.user;

import com.hamdan.agiticket.domain.user.permission.EUserRole;

public record UserDataDto(Long id, String userName, EUserRole role, boolean isUserEnabled) {

    public UserDataDto(User user) {
        this(user.getId(), user.getUsername(), user.getUserRole(), user.isEnabled());
    }

}
