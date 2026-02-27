package com.hamdan.agiticket.domain.comment;

import java.time.LocalDateTime;

public record CommentDataDto(Long id, Long ticketId, String author, String content, LocalDateTime createdAt, LocalDateTime lastEditedAt) {

    public CommentDataDto(Comment comment) {
        this(comment.getId(), comment.getTicket().getId(), comment.getAuthor().getUsername(), comment.getContent(), comment.getCreatedAt(), comment.getLastEditedAt());
    }

}
