package com.hamdan.agiticket.domain.user;

import com.hamdan.agiticket.api.exception.ApiErrorException;
import com.hamdan.agiticket.api.exception.EApiErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(NewUserDto newUserDto) {
        var user = userRepository.findByUserName(newUserDto.userName());

        if (user != null)
            throw new ApiErrorException(HttpStatus.CONFLICT, "Já existe um usuário cadastrado com este nome.");

        user = User.from(newUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Page<UserDataDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDataDto::new);
    }

    public UserDataDto findUser(Long id) {
        return new UserDataDto(findUserById(id));
    }

    public UserDataDto updateUser(UserUpdateDataDto userUpdateDataDto) {
        var user = findUserById(userUpdateDataDto.id());

        if (userUpdateDataDto.newPassword() != null) {
            if (!passwordEncoder.matches(userUpdateDataDto.password(), user.getPassword()))
                throw new ApiErrorException(HttpStatus.UNAUTHORIZED, EApiErrorMessage.INCORRECT_PASSWORD);
            user.setPassword(passwordEncoder.encode(userUpdateDataDto.newPassword()));
        }

        if (userUpdateDataDto.role() != null && !userUpdateDataDto.role().equals(user.getUserRole())) {
            if (!user.getUserRole().isUserAdmin())
                throw new ApiErrorException(HttpStatus.UNAUTHORIZED, "Sem permissão para alterar privilégio.");

            user.setUserRole(userUpdateDataDto.role());
        }

        user = userRepository.save(user);

        return new UserDataDto(user);
    }

    public void toggleUser(Long id) {
        var user = findUserById(id);
        user.setUserEnabled(!user.isUserEnabled());
        userRepository.save(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
