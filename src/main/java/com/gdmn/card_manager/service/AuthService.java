package com.gdmn.card_manager.service;

import com.gdmn.card_manager.dto.AuthenticationRequest;
import com.gdmn.card_manager.dto.Register;

public interface AuthService {
    boolean registerNewUser(Register register);

    String authenticate(AuthenticationRequest authenticationRequest);
}
