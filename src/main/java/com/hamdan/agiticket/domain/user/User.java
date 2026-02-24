package com.hamdan.agiticket.domain.user;

import com.hamdan.agiticket.domain.user.permission.EUserRole;
import com.hamdan.agiticket.domain.user.permission.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "\"user\"")
@Entity(name = "User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private EUserRole userRole;

    private boolean userEnabled;

    public static User from(NewUserDto newUserDto) {
        return from(newUserDto, true);
    }

    public static User from(NewUserDto newUserDto, boolean isUserEnabled) {
        return new User(null, newUserDto.userName(), newUserDto.password(), newUserDto.role(), isUserEnabled);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(UserRole.from(userRole.ROLE));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEnabled;
    }

}
