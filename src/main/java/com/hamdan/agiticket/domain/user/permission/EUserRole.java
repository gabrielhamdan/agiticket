package com.hamdan.agiticket.domain.user.permission;

import java.util.Objects;

public enum EUserRole {

    ADMIN,
    TECH,
    USER;

    public final String ROLE;

    EUserRole() {
        ROLE = String.format("ROLE_%s", name());
    }

    public boolean isUserAdmin() {
        return this == ADMIN;
    }

}
