package com.gdmn.card_manager.exseption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameAlreadyExistsException extends CardManagerException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
