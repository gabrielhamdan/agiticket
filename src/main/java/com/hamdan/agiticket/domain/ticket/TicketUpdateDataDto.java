package com.hamdan.agiticket.domain.ticket;

import jakarta.validation.constraints.NotNull;

public record TicketUpdateDataDto(
        @NotNull Long id,
        String title,
        String description,
        ETicketPriority priority,
        ETickeStatus status
) {
}
