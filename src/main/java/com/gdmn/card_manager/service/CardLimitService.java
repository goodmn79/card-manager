package com.gdmn.card_manager.service;

import com.gdmn.card_manager.enums.LimitType;
import com.gdmn.card_manager.model.Card;

import java.math.BigDecimal;

public interface CardLimitService {
    void setCardLimit(Long cardId, LimitType limitType, BigDecimal amount);

    void checkLimit(Card card, BigDecimal amount, LimitType limitType);
}
