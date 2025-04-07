package com.gdmn.card_manager.service.impl;

import com.gdmn.card_manager.enums.LimitType;
import com.gdmn.card_manager.enums.TransactionType;
import com.gdmn.card_manager.exseption.LimitExceededException;
import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.CardLimit;
import com.gdmn.card_manager.model.Transaction;
import com.gdmn.card_manager.repository.CardLimitRepository;
import com.gdmn.card_manager.repository.TransactionRepository;
import com.gdmn.card_manager.service.CardLimitService;
import com.gdmn.card_manager.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CardLimitServiceImpl implements CardLimitService {
    private final CardLimitRepository cardLimitRepository;
    private final CardService cardService;
    private final TransactionRepository transactionRepository;

    @Override
    public void setCardLimit(Long cardId, LimitType limitType, BigDecimal amount) {
        Card card = cardService.findById(cardId);

        CardLimit limit =
                cardLimitRepository.findByCardAndLimitType(card, limitType)
                        .orElse(new CardLimit());

        limit
                .setCard(card)
                .setLimitType(limitType)
                .setAmount(amount);
        this.updateResetDate(limit);

        cardLimitRepository.save(limit);
    }

    @Override
    public void checkLimit(Card card, BigDecimal amount, LimitType limitType) {
        cardLimitRepository.findByCardAndLimitType(card, limitType)
                .ifPresent(cardLimit -> {
                    if (LocalDate.now().isAfter(cardLimit.getResetDate())) {
                        this.resetLimit(cardLimit);
                    }

                    BigDecimal usedAmount = this.calculateUsedAmount(card, limitType, cardLimit.getResetDate());
                    BigDecimal remaining = cardLimit.getAmount().subtract(usedAmount);

                    if (remaining.compareTo(amount) < 0) {
                        throw new LimitExceededException();
                    }
                });
    }

    private BigDecimal calculateUsedAmount(Card card, LimitType limitType, LocalDate resetDate) {
        LocalDateTime startDate = this.getStartDateForLimitType(limitType, resetDate);

        List<Transaction> transactions = transactionRepository
                .findByCardAndTypeAndTimestampAfter(
                        card,
                        this.getTransactionTypeForLimitType(limitType),
                        startDate);

        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void resetLimit(CardLimit limit) {
        this.updateResetDate(limit);
        cardLimitRepository.save(limit);
    }

    private void updateResetDate(CardLimit limit) {
        LocalDate now = LocalDate.now();
        switch (limit.getLimitType()) {
            case DAILY_WITHDRAWAL:
            case DAILY_TRANSFER:
                limit.setResetDate(now.plusDays(1));
                break;
            case MONTHLY_WITHDRAWAL:
            case MONTHLY_TRANSFER:
                limit.setResetDate(now.withDayOfMonth(1).plusMonths(1));
                break;
        }
    }

    private LocalDateTime getStartDateForLimitType(LimitType limitType, LocalDate resetDate) {
        return switch (limitType) {
            case DAILY_WITHDRAWAL, DAILY_TRANSFER -> resetDate.minusDays(1).atStartOfDay();
            case MONTHLY_WITHDRAWAL, MONTHLY_TRANSFER -> resetDate.minusMonths(1).withDayOfMonth(1).atStartOfDay();
        };
    }

    private TransactionType getTransactionTypeForLimitType(LimitType limitType) {
        return switch (limitType) {
            case DAILY_WITHDRAWAL, MONTHLY_WITHDRAWAL -> TransactionType.WITHDRAW;
            case DAILY_TRANSFER, MONTHLY_TRANSFER -> TransactionType.TRANSFER;
        };
    }
}
