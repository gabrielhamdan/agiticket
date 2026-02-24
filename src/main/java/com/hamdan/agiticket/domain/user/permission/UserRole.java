package com.hamdan.agiticket.domain.user.permission;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

public class UserRole implements GrantedAuthority {

    private final String role;

    private UserRole(String role) {
        this.role = role;
    }

    public static UserRole from(String role) {
        Assert.hasText(role, "");
        return new UserRole(role);
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof SimpleGrantedAuthority) {
            SimpleGrantedAuthority sga = (SimpleGrantedAuthority)obj;
            return role.equals(sga.getAuthority());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return role.hashCode();
    }

}
