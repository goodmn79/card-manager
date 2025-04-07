package com.gdmn.card_manager.exseption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserRequestNotFoundException extends CardManagerException {
    public UserRequestNotFoundException(String message) {
        super(message);
    }
}
