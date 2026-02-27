package com.hamdan.agiticket.domain.ticket;

import com.hamdan.agiticket.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByIdAndAuthor(Long id, User user);

    Page<Ticket> findAllByAuthor(User user, Pageable pageable);

}
