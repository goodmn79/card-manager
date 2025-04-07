package com.gdmn.card_manager.mapper;

import com.gdmn.card_manager.dto.CardLimitData;
import com.gdmn.card_manager.model.CardLimit;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CardLimitMapper {

    public CardLimitData map(CardLimit cardLimit) {
        return new CardLimitData()
                .setId(cardLimit.getId())
                .setLimitType(cardLimit.getLimitType())
                .setAmount(cardLimit.getAmount());
    }

    public Set<CardLimitData> map(Set<CardLimit> cardLimits) {
        return cardLimits
                .stream()
                .map(this::map)
                .collect(Collectors.toSet());
    }
}
