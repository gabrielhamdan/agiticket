package com.hamdan.agiticket.domain.ticket;

import com.hamdan.agiticket.api.exception.ApiAssert;
import com.hamdan.agiticket.api.exception.ApiErrorException;
import com.hamdan.agiticket.api.response.ApiPaginationDto;
import com.hamdan.agiticket.domain.user.User;
import com.hamdan.agiticket.domain.user.UserService;
import com.hamdan.agiticket.domain.user.permission.EUserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    private final UserService userService;

    public TicketService(TicketRepository ticketRepository, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
    }

    public TicketDataDto createTicket(User author, NewTicketDataDto newTicket) {
        var ticket = Ticket.from(newTicket);
        ticket.setAuthor(author);

        if (newTicket.assignee() != null) {
            ApiAssert.notEquals(author.getUserRole(), EUserRole.USER, () -> new ApiErrorException(HttpStatus.FORBIDDEN, "Usuário sem permissão para atribuir um responsável pelo chamado."));

            var assignee = userService.findUserByIdOrElseThrow(newTicket.assignee());

            ApiAssert.notEquals(assignee.getUserRole(), EUserRole.USER,  () -> new ApiErrorException(HttpStatus.FORBIDDEN, "Não é possível atribuir este usuário como responsável do chamado."));

            ticket.setAssignee(assignee);
        }

        ticket = ticketRepository.save(ticket);

        log.info("Ticket {} created by user {}", ticket.getId(), author.getId());

        return new TicketDataDto(ticket);
    }

    public TicketDataDto findTicket(User user, Long id) {
        if (user.getUserRole().equals(EUserRole.USER)) {
            var ticket = ticketRepository.findByIdAndAuthor(id, user).orElseThrow(EntityNotFoundException::new);

            return new TicketDataDto(ticket);
        }

        return new TicketDataDto(findTicketByIdOrElseThrow(id));
    }

    public ApiPaginationDto<TicketDataDto> findAll(User user, Pageable pageable) {
        if (user.getUserRole().equals(EUserRole.USER)) {
            var page = ticketRepository.findAllByAuthor(user, pageable);

            return new ApiPaginationDto<>(page, TicketDataDto::new);
        }

        var page = ticketRepository.findAll(pageable);

        return new ApiPaginationDto<>(page, TicketDataDto::new);
    }

    public TicketDataDto editTicket(User user, TicketUpdateDataDto ticketDto) {
        var ticket = findTicketByIdOrElseThrow(ticketDto.id());

        ApiAssert.notEquals(ticket.getStatus(), ETickeStatus.CLOSED,  () -> new ApiErrorException(HttpStatus.BAD_REQUEST, "Não é possível alterar um chamado encerrado."));

        if (ticketDto.status() != null) {
            if (user.getUserRole().equals(EUserRole.USER) && !ticketDto.status().equals(ETickeStatus.CLOSED) && !ticketDto.status().equals(ticket.getStatus()))
                throw new ApiErrorException(HttpStatus.FORBIDDEN, "Usuário sem permissão para o status do chamado.");

            if (ticketDto.status().equals(ETickeStatus.IN_PROGRESS))
                ApiAssert.notNull(ticket.getAssignee(), () -> new ApiErrorException(HttpStatus.BAD_REQUEST, "Não é possível iniciar um chamado sem a atribuição de um usuário responsável."));

            ticket.setStatus(ticketDto.status());
        }

        ticket.update(ticketDto);

        ticket = ticketRepository.save(ticket);

        return new TicketDataDto(ticket);
    }

    public TicketDataDto editTicketAssignee(TicketAssigneeDto ticketAssigneeDto) {
        var ticket = findTicketByIdOrElseThrow(ticketAssigneeDto.id());

        ApiAssert.notEquals(ticket.getStatus(), ETickeStatus.CLOSED,  () -> new ApiErrorException(HttpStatus.BAD_REQUEST, "Não é possível alterar um chamado encerrado."));

        var assignee = userService.findUserByIdOrElseNull(ticketAssigneeDto.assigneeId());

        ApiAssert.notEquals(assignee.getUserRole(), EUserRole.USER,  () -> new ApiErrorException(HttpStatus.FORBIDDEN, "Não é possível atribuir este usuário como responsável do chamado."));

        ticket.setAssignee(assignee);
        ticket.setUpdatedAt(LocalDateTime.now());

        ticket = ticketRepository.save(ticket);

        return new TicketDataDto(ticket);
    }

    public TicketDataDto reopenTicket(User authPrincipal, Long id) {
        var ticket = findTicketByIdOrElseThrow(id);

        ticket.setStatus(ETickeStatus.OPEN);

        ticket = ticketRepository.save(ticket);

        log.info("User {} reopened ticket {}", authPrincipal.getId(), ticket.getId());

        return new TicketDataDto(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.delete(findTicketByIdOrElseThrow(id));
    }

    private Ticket findTicketByIdOrElseThrow(Long id) {
        return ticketRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
