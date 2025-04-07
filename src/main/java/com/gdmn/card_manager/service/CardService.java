package com.gdmn.card_manager.service;

import com.gdmn.card_manager.dto.CardData;
import com.gdmn.card_manager.dto.CreateCard;
import com.gdmn.card_manager.model.Card;
import org.springframework.data.domain.Page;

public interface CardService {
    void addPaymentCard(CreateCard card);

    CardData getById(long cardId);

    Card findById(long id);

    Page<CardData> getAll(int page, int size);

    Page<CardData> getAllUserCards(int page, int size);

    void changeStatus(long cardId);

    void updateCard(Card card);

    void deleteCard(long cardId);

    void checkCardStatus(Card card);

    void verifyCardOwnership(Card card);
}
