package com.gdmn.card_manager.exseption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends CardManagerException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
