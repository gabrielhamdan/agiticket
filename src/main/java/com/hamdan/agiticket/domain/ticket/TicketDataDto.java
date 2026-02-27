package com.hamdan.agiticket.domain.ticket;

import java.time.LocalDateTime;

public record TicketDataDto(
    Long id,
    String title,
    String description,
    ETickeStatus status,
    ETicketPriority priority,
    String author,
    String assignee,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public TicketDataDto(Ticket ticket) {
        this(ticket.getId(), ticket.getTitle(), ticket.getDescription(),
                ticket.getStatus(), ticket.getPriority(),
                ticket.getAuthor().getUsername(),
                ticket.getAssignee() != null ? ticket.getAssignee().getUsername() : null,
                ticket.getCreatedAt(), ticket.getUpdatedAt());
    }

}
