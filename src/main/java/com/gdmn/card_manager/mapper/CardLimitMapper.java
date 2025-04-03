package com.gdmn.card_manager.mapper;

import com.gdmn.card_manager.dto.CardLimitData;
import com.gdmn.card_manager.model.CardLimit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardLimitMapper {

    public CardLimitData map(CardLimit cardLimit) {
        return new CardLimitData()
                .setId(cardLimit.getId())
                .setLimitType(cardLimit.getLimitType())
                .setAmount(cardLimit.getAmount());
    }

    public List<CardLimitData> map(List<CardLimit> cardLimits) {
        return cardLimits
                .stream()
                .map(this::map)
                .toList();
    }
}
