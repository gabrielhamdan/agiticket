package com.hamdan.agiticket.api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hamdan.agiticket.api.exception.ApiErrorException;
import com.hamdan.agiticket.api.exception.EApiErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TokenUtils {

    public static Instant getExpiration(int seconds, ZoneOffset zoneOffset) {
        return LocalDateTime.now().plusSeconds(seconds).toInstant(zoneOffset);
    }

    public static String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null)
            return authorizationHeader.replace("Bearer ", "");

        return null;
    }

    public static String getSubject(Algorithm algorithm, String issuer, String jwt) {
        try {
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(jwt)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new ApiErrorException(HttpStatus.UNAUTHORIZED, EApiErrorMessage.INVALID_TOKEN);
        }
    }

    public static Claim getClaim(String claimKey, Algorithm algorithm, String issuer, String jwt) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(jwt);

            return decodedJWT.getClaim(claimKey);
        } catch (JWTVerificationException exception) {
            throw new ApiErrorException(HttpStatus.UNAUTHORIZED, EApiErrorMessage.INVALID_TOKEN);
        }
    }

}
