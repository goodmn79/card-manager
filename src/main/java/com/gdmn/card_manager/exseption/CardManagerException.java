package com.gdmn.card_manager.exseption;

public abstract class CardManagerException extends RuntimeException {
    public CardManagerException() {
    }

    public CardManagerException(String message) {
        super(message);
    }
}
