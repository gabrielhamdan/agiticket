package com.hamdan.agiticket.controllers;

import com.hamdan.agiticket.api.misc.ApiResponseDto;
import com.hamdan.agiticket.domain.user.NewUserDto;
import com.hamdan.agiticket.domain.user.UserDataDto;
import com.hamdan.agiticket.domain.user.UserService;
import com.hamdan.agiticket.domain.user.UserUpdateDataDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Este método permite que um usuário ADMIN crie um novo usuário no sistema.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> createUser(@RequestBody @Valid NewUserDto newUserDto) {
        userService.createUser(newUserDto);
        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.ACCEPTED, "Usuário criado com sucesso."));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Este método pesquisa um usuário por ID.")
    public ResponseEntity<UserDataDto> findUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUser(id));
    }

    @GetMapping
    @Operation(summary = "Este método lista todos os usuários do sistema.")
    public ResponseEntity<Page<UserDataDto>> findAllUsers(@PageableDefault(size = 10, sort = {"userName"}) Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @PutMapping
    @Operation(summary = "Este método permite alterar usuários do sistema.")
    public ResponseEntity<UserDataDto> updateUser(@RequestBody @Valid UserUpdateDataDto userUpdateDataDto) {
        return ResponseEntity.ok(userService.updateUser(userUpdateDataDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Este método permite que um usuário ADMIN ative/desative usuários do sistema.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto> toggleUser(@PathVariable Long id) {
        userService.toggleUser(id);
        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.OK, "Usuário alterado com sucesso."));
    }

}
