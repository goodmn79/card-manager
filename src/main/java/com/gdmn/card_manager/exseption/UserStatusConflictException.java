package com.gdmn.card_manager.exseption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserStatusConflictException extends CardManagerException {
    public UserStatusConflictException(String message) {
        super(message);
    }
}
