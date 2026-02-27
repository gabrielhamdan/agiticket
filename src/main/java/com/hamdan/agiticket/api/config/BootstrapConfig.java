package com.hamdan.agiticket.api.config;

import com.hamdan.agiticket.api.exception.ApiAssert;
import com.hamdan.agiticket.domain.user.User;
import com.hamdan.agiticket.domain.user.UserRepository;
import com.hamdan.agiticket.domain.user.permission.EUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class BootstrapConfig {

    @Bean
    CommandLineRunner bootstrapAdmin(
            UserRepository repository,
            PasswordEncoder encoder,
            @Value("${app.bootstrap.admin:false}") boolean bootstrap,
            @Value("${app.bootstrap.admin.username:}") String userName,
            @Value("${app.bootstrap.admin.password:}") String password
    ) {
        return args -> {
            if (!bootstrap || repository.existsByUserRole(EUserRole.ADMIN)) return;

            ApiAssert.isTrue(
                    !userName.isBlank() && !password.isBlank(),
                    () -> new IllegalStateException("Admin bootstrap credentials were not provided."));

            var admin = new User();
            admin.setUserName(userName);
            admin.setPassword(encoder.encode(password));
            admin.setUserRole(EUserRole.ADMIN);
            admin.setUserEnabled(true);

            repository.save(admin);

            log.info("Admin {} successfully created on bootstrap.", userName);
        };
    }

}
