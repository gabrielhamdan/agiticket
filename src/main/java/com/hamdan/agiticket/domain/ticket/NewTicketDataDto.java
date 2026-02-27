package com.hamdan.agiticket.domain.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewTicketDataDto(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull ETicketPriority priority,
        Long assignee
) {
}
