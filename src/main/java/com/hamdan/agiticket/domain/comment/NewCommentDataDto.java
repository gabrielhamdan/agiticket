package com.hamdan.agiticket.domain.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCommentDataDto(@NotNull Long ticketId, @NotBlank String content) {
}
