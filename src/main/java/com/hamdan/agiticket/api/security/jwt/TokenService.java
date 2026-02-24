package com.hamdan.agiticket.api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.hamdan.agiticket.domain.user.User;
import com.hamdan.agiticket.domain.user.auth.TokenDto;
import com.hamdan.agiticket.api.exception.ApiErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

//    @Value("${api.security.token.secret}")
    // FIXME proper secret
    public static final String SECRET = "teste123";

    public static final int TOKEN_DURATION = 60 * 15;
    public static final String TOKEN_ISSUER = "AgiTicket API";
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("-03:00");
    public static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public TokenDto issueToken(User user) {
        try {
            var expiration = TokenUtils.getExpiration(TOKEN_DURATION, ZONE_OFFSET);

            var jwt = JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withSubject(user.getUsername())
                    .withExpiresAt(expiration)
                    .sign(ALGORITHM);

            return new TokenDto(jwt, LocalDateTime.ofInstant(expiration, ZONE_OFFSET));
        } catch (JWTCreationException exception) {
            throw new ApiErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado ao gerar o token de autenticação.");
        }
    }

}
