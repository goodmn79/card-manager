package com.gdmn.card_manager.service.impl;

import com.gdmn.card_manager.enums.Role;
import com.gdmn.card_manager.exseption.AccountDisabledException;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);

        if (!user.isEnabled()) {
            throw new AccountDisabledException("Account is disabled");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role) {
        return Stream.of(role.name())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
