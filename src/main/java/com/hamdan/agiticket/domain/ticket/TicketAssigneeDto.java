package com.hamdan.agiticket.domain.ticket;

import jakarta.validation.constraints.NotNull;

public record TicketAssigneeDto(
        @NotNull Long id,
        @NotNull Long assigneeId
) {
}
