package com.hamdan.agiticket.api.security;

import com.hamdan.agiticket.domain.user.UserRepository;
import com.hamdan.agiticket.api.security.jwt.TokenService;
import com.hamdan.agiticket.api.security.jwt.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public SecurityFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = TokenUtils.getToken(request);

        if (token != null) {
            var userName = TokenUtils.getSubject(TokenService.ALGORITHM, TokenService.TOKEN_ISSUER, token);

            var user = userRepository.findByUserName(userName);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
