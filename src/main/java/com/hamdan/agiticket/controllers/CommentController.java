package com.hamdan.agiticket.controllers;

import com.hamdan.agiticket.api.response.ApiPaginationDto;
import com.hamdan.agiticket.api.response.ApiResponseDto;
import com.hamdan.agiticket.domain.comment.CommentDataDto;
import com.hamdan.agiticket.domain.comment.CommentService;
import com.hamdan.agiticket.domain.comment.CommentUpdateDataDto;
import com.hamdan.agiticket.domain.comment.NewCommentDataDto;
import com.hamdan.agiticket.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@Tag(name = "Comentários")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @Operation(summary = "Este método permite que um usuário crie um novo comentário em um chamado.")
    public ResponseEntity<CommentDataDto> createComment(@AuthenticationPrincipal User user, @Valid NewCommentDataDto newCommentDataDto) {
        return ResponseEntity.ok(commentService.createComment(user, newCommentDataDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Este método pesquisa uma comentário por ID.")
    public ResponseEntity<CommentDataDto> findComment(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.findComment(user, id));
    }

    @GetMapping
    @Operation(summary = "Este método lista todos os comentários de um chamado.")
    public ResponseEntity<ApiPaginationDto<CommentDataDto>> findAllComments(@AuthenticationPrincipal User user, @RequestParam Long ticketId, @PageableDefault(sort = {"id"}) Pageable pageable) {
        return ResponseEntity.ok(commentService.findAllComments(user, ticketId, pageable));
    }

    @PutMapping
    @Operation(summary = "Este método permite que um usuário altere o comentário de um chamado.")
    public ResponseEntity<CommentDataDto> editComment(@AuthenticationPrincipal User user, @Valid CommentUpdateDataDto commentUpdateDataDto) {
        return ResponseEntity.ok(commentService.editComment(user, commentUpdateDataDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Este método permite que um usuário exclua o comentário de um chamado.")
    public ResponseEntity<ApiResponseDto> deleteComment(@AuthenticationPrincipal User user, @PathVariable Long id) {
        commentService.deleteComment(user, id);
        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.OK, "Comentário excluído com sucesso."));
    }

}
