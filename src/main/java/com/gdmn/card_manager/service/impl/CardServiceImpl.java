package com.gdmn.card_manager.service.impl;

import com.gdmn.card_manager.dto.CardData;
import com.gdmn.card_manager.dto.CreateCard;
import com.gdmn.card_manager.enums.CardStatus;
import com.gdmn.card_manager.exseption.CardNotFoundException;
import com.gdmn.card_manager.exseption.IllegalCardStatusException;
import com.gdmn.card_manager.mapper.CardMapper;
import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.repository.CardRepository;
import com.gdmn.card_manager.service.CardService;
import com.gdmn.card_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserService userService;

    @Override
    public void addPaymentCard(CreateCard card) {
        cardRepository.save(
                cardMapper.map(card)
        );
    }

    @Override
    public CardData getById(long cardId) {
        Card card = this.findById(cardId);
        return cardMapper.map(card);
    }

    @Override
    public Card findById(long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Cards not found"));
    }

    @Override
    public Page<CardData> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("expiryDate").descending());
        Page<Card> cards = cardRepository.findAll(pageable);
        if (cards.getContent().isEmpty()) {
            throw new CardNotFoundException("Cards not found");
        }
        return cardMapper.map(cards);
    }

    @Override
    public Page<CardData> getAllUserCards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("expiryDate").descending());
        User currentUser = userService.currentUser();
        Page<Card> cards =
                cardRepository.findUserCardsSortedByExpiryDate(currentUser.getId(), pageable);
        if (cards.getContent().isEmpty()) {
            throw new CardNotFoundException("Cards not found");
        }
        return cardMapper.map(cards);
    }

    @Override
    public void changeStatus(long cardId) {
        Card card =
                cardRepository.findById(cardId)
                        .orElseThrow(() -> new CardNotFoundException("Cards not found"));
        if (card.getStatus() == CardStatus.ACTIVE) {
            card.setStatus(CardStatus.BLOCKED);
        } else {
            card.setStatus(CardStatus.ACTIVE);
        }
        this.updateCard(card);
    }

    @Override
    public void updateCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(long cardId) {
        Card card =
                cardRepository.findById(cardId)
                        .orElseThrow(() -> new CardNotFoundException("Cards not found"));
        cardRepository.delete(card);
    }

    @Override
    public void checkCardStatus(Card card) {
        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw new IllegalCardStatusException();
        }
    }

    @Override
    public void verifyCardOwnership(Card card) {
        User currentUser = userService.currentUser();
        if (!currentUser.equals(card.getOwner())) {
            throw new PermissionDeniedException();
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void expiredCard() {
        cardRepository.updateExpired();
    }
}
