package com.gdmn.card_manager.service.impl;

import com.gdmn.card_manager.dto.AuthenticationRequest;
import com.gdmn.card_manager.dto.Register;
import com.gdmn.card_manager.secure.JwtTokenUtil;
import com.gdmn.card_manager.service.AuthService;
import com.gdmn.card_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean registerNewUser(Register register) {
        if (userService.existsByUsername(register.getUsername())) {
            return false;
        }
        userService.addUser(register);
        return true;
    }

    @Override
    public String authenticate(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtTokenUtil.generateToken(userDetails);
    }
}
