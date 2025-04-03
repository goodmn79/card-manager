package com.gdmn.card_manager.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    DEPOSIT("Deposit operation"),
    WITHDRAW("Withdrawal operation"),
    TRANSFER("Transfer operation");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

}
