package com.hamdan.agiticket.controllers;

import com.hamdan.agiticket.api.response.ApiPaginationDto;
import com.hamdan.agiticket.api.response.ApiResponseDto;
import com.hamdan.agiticket.domain.ticket.*;
import com.hamdan.agiticket.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Chamados")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @Operation(summary = "Este método permite que um usuário crie um novo chamado.")
    public ResponseEntity<TicketDataDto> createTicket(@AuthenticationPrincipal User user, @Valid NewTicketDataDto newTicketDataDto) {
        return ResponseEntity.ok(ticketService.createTicket(user, newTicketDataDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Este método pesquisa um chamado por ID.")
    public ResponseEntity<TicketDataDto> findTicket(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(ticketService.findTicket(user, id));
    }

    @GetMapping
    @Operation(summary = "Este método lista todos os chamados.")
    public ResponseEntity<ApiPaginationDto<TicketDataDto>> findAllTickets(@AuthenticationPrincipal User user, @PageableDefault(sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(ticketService.findAll(user, pageable));
    }

    @PutMapping
    @Operation(summary = "Este método permite que um usuário altere um chamado.")
    public ResponseEntity<TicketDataDto> updateTicket(@AuthenticationPrincipal User user, @Valid TicketUpdateDataDto ticketUpdateDataDto) {
        return ResponseEntity.ok(ticketService.editTicket(user, ticketUpdateDataDto));
    }

    @PutMapping("/assignee")
    @Operation(summary = "Este método permite que um usuário ADMIN/TECH altere o responsável por um chamado.")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('TECH')")
    public ResponseEntity<TicketDataDto> updateAssignee(@Valid TicketAssigneeDto ticketAssigneeDto) {
        return ResponseEntity.ok(ticketService.editTicketAssignee(ticketAssigneeDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Este método permite que um usuário ADMIN reabra um chamado.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketDataDto> reopenTicket(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(ticketService.reopenTicket(user, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Este método permite que um usuário ADMIN exclua um chamado.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.OK, "Chamado excluído com sucesso."));
    }

}
