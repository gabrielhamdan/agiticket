package com.hamdan.agiticket.domain.user;

import com.hamdan.agiticket.domain.user.permission.EUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    boolean existsByUserRole(EUserRole role);

}
