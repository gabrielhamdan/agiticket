package com.hamdan.agiticket.domain.comment;

import com.hamdan.agiticket.domain.ticket.Ticket;
import com.hamdan.agiticket.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndAuthor(Long id, User author);

    Page<Comment> findAllByTicketAndAuthor(Ticket ticket, User author, Pageable pageable);

    Page<Comment> findAllByTicket(Ticket ticket, Pageable pageable);

}
