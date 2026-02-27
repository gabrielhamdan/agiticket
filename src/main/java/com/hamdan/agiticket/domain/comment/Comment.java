package com.hamdan.agiticket.domain.comment;

import com.hamdan.agiticket.domain.ticket.Ticket;
import com.hamdan.agiticket.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "comment")
@Entity(name = "Comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    private LocalDateTime createdAt;

    private LocalDateTime lastEditedAt;

    public static Comment from(NewCommentDataDto newCommentDataDto) {
        return new Comment(null, newCommentDataDto.content(), null, null, LocalDateTime.now(), null);
    }

}
