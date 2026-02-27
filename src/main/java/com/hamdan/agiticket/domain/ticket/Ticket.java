package com.hamdan.agiticket.domain.ticket;

import com.hamdan.agiticket.domain.comment.Comment;
import com.hamdan.agiticket.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "ticket")
@Entity(name = "Ticket")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private ETickeStatus status;

    @Enumerated(EnumType.STRING)
    private ETicketPriority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignee;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments;

    public static Ticket from(NewTicketDataDto newTicket) {
        return new Ticket(null, newTicket.title(), newTicket.description(), ETickeStatus.OPEN, newTicket.priority(),
                null, null, LocalDateTime.now(), LocalDateTime.now(), null);
    }

    public void update(TicketUpdateDataDto ticketDto) {
        title = ticketDto.title() == null ? title : ticketDto.title();
        description = ticketDto.description() == null ? title : ticketDto.description();
        priority = ticketDto.priority() == null ? priority : ticketDto.priority();
        updatedAt = LocalDateTime.now();
    }
}
