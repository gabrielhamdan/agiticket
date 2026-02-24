package com.hamdan.agiticket.domain.user.auth;

import java.time.LocalDateTime;

public record TokenDto(String token, LocalDateTime expiresAt) {
}
