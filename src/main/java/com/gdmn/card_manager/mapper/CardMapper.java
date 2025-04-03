package com.gdmn.card_manager.mapper;

import com.gdmn.card_manager.dto.ActivateCard;
import com.gdmn.card_manager.dto.CardData;
import com.gdmn.card_manager.dto.CardLimitData;
import com.gdmn.card_manager.dto.CreateCard;
import com.gdmn.card_manager.enums.CardStatus;
import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CardMapper {
    private final StringEncryptor stringEncryptor;
    private final UserService userService;
    private final CardLimitMapper cardLimitMapper;

    public Card map(CreateCard card) {
        String encryptedCardNumber = stringEncryptor.encrypt(card.getCardNumber());
        return new Card()
                .setCardNumber(encryptedCardNumber);
    }

    public Card map(ActivateCard activateCard, Card card) {
        User user = userService.getById(activateCard.getUserId());
        return card
                .setUser(user)
                .setExpiryDate(LocalDate.now().plusYears(3))
                .setStatus(CardStatus.ACTIVE)
                .setBalance(activateCard.getBalance());
    }

    public CardData map(Card card) {
        String maskedCardNumber = this.getMaskedCardNumber(card.getCardNumber());
        List<CardLimitData> limits = cardLimitMapper.map(card.getLimits());
        return new CardData()
                .setId(card.getId())
                .setCardNumber(maskedCardNumber)
                .setUserId(card.getUser().getId())
                .setExpiryDate(card.getExpiryDate())
                .setStatus(card.getStatus())
                .setBalance(card.getBalance())
                .setLimits(limits);
    }

    public Page<CardData> map(Page<Card> paymentCardPage) {
        return paymentCardPage.map(this::map);
    }

    private String getMaskedCardNumber(String cardNumber) {
        String decodedCardNumber = stringEncryptor.decrypt(cardNumber);
        if (decodedCardNumber == null || decodedCardNumber.length() < 8) {
            return "****";
        }
        return "**** **** **** " + decodedCardNumber.substring(decodedCardNumber.length() - 4);
    }
}
