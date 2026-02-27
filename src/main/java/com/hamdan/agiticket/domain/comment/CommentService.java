package com.hamdan.agiticket.domain.comment;

import com.hamdan.agiticket.api.exception.ApiAssert;
import com.hamdan.agiticket.api.exception.ApiErrorException;
import com.hamdan.agiticket.api.response.ApiPaginationDto;
import com.hamdan.agiticket.domain.ticket.Ticket;
import com.hamdan.agiticket.domain.ticket.TicketRepository;
import com.hamdan.agiticket.domain.user.User;
import com.hamdan.agiticket.domain.user.permission.EUserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final TicketRepository ticketRepository;

    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    public CommentDataDto createComment(User user, NewCommentDataDto commentDataDto) {
        Ticket ticket;

        if (user.getUserRole().equals(EUserRole.USER))
            ticket = ticketRepository.findByIdAndAuthor(commentDataDto.ticketId(), user).orElse(null);
        else
            ticket = ticketRepository.findById(commentDataDto.ticketId()).orElse(null);

        ApiAssert.notNull(ticket, EntityNotFoundException::new);

        var comment = Comment.from(commentDataDto);
        comment.setAuthor(user);
        comment.setTicket(ticket);

        comment = commentRepository.save(comment);

        return new CommentDataDto(comment);
    }

    public CommentDataDto findComment(User user, Long id) {
        Comment comment;

        if (user.getUserRole().equals(EUserRole.USER))
            comment = commentRepository.findByIdAndAuthor(id, user).orElse(null);
        else
            comment = commentRepository.findById(id).orElse(null);

        ApiAssert.notNull(comment, EntityNotFoundException::new);

        return new CommentDataDto(comment);
    }

    public ApiPaginationDto<CommentDataDto> findAllComments(User user, Long ticketId, Pageable pageable) {
        Ticket ticket;

        if (user.getUserRole().equals(EUserRole.USER))
            ticket = ticketRepository.findByIdAndAuthor(ticketId, user).orElse(null);
        else
            ticket = ticketRepository.findById(ticketId).orElse(null);

        ApiAssert.notNull(ticket, EntityNotFoundException::new);

        Page<Comment> comments;

        if (user.getUserRole().equals(EUserRole.USER))
            comments = commentRepository.findAllByTicketAndAuthor(ticket, user, pageable);
        else
            comments = commentRepository.findAllByTicket(ticket, pageable);

        return new ApiPaginationDto<>(comments, CommentDataDto::new);
    }

    public CommentDataDto editComment(User user, CommentUpdateDataDto commentUpdateDataDto) {
        Comment comment = commentRepository.findById(commentUpdateDataDto.id()).orElseThrow(EntityNotFoundException::new);

        ApiAssert.equals(comment.getAuthor(), user, () -> new ApiErrorException(HttpStatus.FORBIDDEN, "Usuário sem permissão para editar o comentário."));

        comment.setContent(commentUpdateDataDto.content());
        comment.setLastEditedAt(LocalDateTime.now());

        comment = commentRepository.save(comment);

        return new CommentDataDto(comment);
    }

    public void deleteComment(User user, Long id) {
        var comment = commentRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (!user.getUserRole().equals(EUserRole.ADMIN))
            ApiAssert.equals(comment.getAuthor(), user, () -> new ApiErrorException(HttpStatus.FORBIDDEN, "Usuário sem permissão para excluir o comentário."));

        commentRepository.delete(comment);

        log.warn("Comment {} deleted by user {}", comment.getId(), user.getId());
    }

}
