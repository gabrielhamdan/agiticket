package com.hamdan.agiticket.controllers;

import com.hamdan.agiticket.api.security.jwt.TokenService;
import com.hamdan.agiticket.domain.user.User;
import com.hamdan.agiticket.domain.user.auth.TokenDto;
import com.hamdan.agiticket.domain.user.auth.UserCredentialsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação")
public class AuthController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AuthController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    @Operation(summary = "Este método gera um token de autenticação para o usuário.")
    public ResponseEntity<TokenDto> issueToken(@RequestBody @Valid UserCredentialsDto userCredentialsDto) {
        var authToken = new UsernamePasswordAuthenticationToken(userCredentialsDto.userName(), userCredentialsDto.password());
        var authentication = authenticationManager.authenticate(authToken);
        var user = (User)authentication.getPrincipal();

        return ResponseEntity.ok(tokenService.issueToken(user));
    }

}
