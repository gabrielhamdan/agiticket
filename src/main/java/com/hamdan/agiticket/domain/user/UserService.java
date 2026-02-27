package com.hamdan.agiticket.domain.user;

import com.hamdan.agiticket.api.exception.ApiAssert;
import com.hamdan.agiticket.api.exception.ApiErrorException;
import com.hamdan.agiticket.api.exception.EApiErrorMessage;
import com.hamdan.agiticket.api.response.ApiPaginationDto;
import com.hamdan.agiticket.domain.user.permission.EUserRole;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDataDto createUser(User authPrincipal, NewUserDto newUserDto) {
        var user = userRepository.findByUserName(newUserDto.userName());

        if (user != null)
            throw new ApiErrorException(HttpStatus.CONFLICT, "Já existe um usuário cadastrado com este nome.");

        user = User.from(newUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        log.info("User {} created user with id={} and role={}", authPrincipal.getId(), user.getId(), user.getUserRole());

        return new UserDataDto(user);
    }

    public ApiPaginationDto<UserDataDto> findAll(Pageable pageable) {
        var page = userRepository.findAll(pageable);

        return new ApiPaginationDto<>(page, UserDataDto::new);
    }

    public UserDataDto findUser(Long id) {
        return new UserDataDto(findUserByIdOrElseThrow(id));
    }

    public UserDataDto updateUser(User authPrincipal, UserUpdateDataDto userUpdateDataDto) {
        var user = findUserByIdOrElseThrow(userUpdateDataDto.id());

        if (!authPrincipal.equals(user))
            ApiAssert.isTrue(authPrincipal.getUserRole().equals(EUserRole.ADMIN));

        if (userUpdateDataDto.newPassword() != null) {
            if (!passwordEncoder.matches(userUpdateDataDto.password(), user.getPassword()) && !authPrincipal.getUserRole().equals(EUserRole.ADMIN))
                throw new ApiErrorException(HttpStatus.UNAUTHORIZED, EApiErrorMessage.INCORRECT_PASSWORD);

            user.setPassword(passwordEncoder.encode(userUpdateDataDto.newPassword()));
        }

        if (userUpdateDataDto.role() != null && !userUpdateDataDto.role().equals(user.getUserRole())) {
            ApiAssert.isTrue(authPrincipal.getUserRole().isUserAdmin(), () -> new ApiErrorException(HttpStatus.FORBIDDEN, "Sem permissão para alterar privilégio."));

            user.setUserRole(userUpdateDataDto.role());
        }

        user = userRepository.save(user);

        log.info("User {} changed role of user {} to {}", authPrincipal.getId(), user.getId(), user.getUserRole());

        return new UserDataDto(user);
    }

    public void toggleUser(User authPrincipal, Long id) {
        var user = findUserByIdOrElseThrow(id);
        user.setUserEnabled(!user.isUserEnabled());
        userRepository.save(user);
        log.info("User {} toggled {} status of user {}", authPrincipal.getId(), user.isUserEnabled() ? "enabled" : "disabled", user.getId());
    }

    public User findUserByIdOrElseThrow(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public User findUserByIdOrElseNull(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
