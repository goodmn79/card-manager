package com.gdmn.card_manager.service.impl;

import com.gdmn.card_manager.dto.TransactionData;
import com.gdmn.card_manager.enums.LimitType;
import com.gdmn.card_manager.enums.TransactionType;
import com.gdmn.card_manager.exseption.InsufficientFundsException;
import com.gdmn.card_manager.exseption.TransactionNotFoundException;
import com.gdmn.card_manager.mapper.TransactionMapper;
import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.Transaction;
import com.gdmn.card_manager.repository.TransactionRepository;
import com.gdmn.card_manager.service.CardLimitService;
import com.gdmn.card_manager.service.CardService;
import com.gdmn.card_manager.service.TransactionService;
import com.gdmn.card_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.gdmn.card_manager.enums.TransactionType.DEPOSIT;
import static com.gdmn.card_manager.enums.TransactionType.WITHDRAW;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final CardService cardService;
    private final TransactionRepository transactionRepository;
    private final CardLimitService cardLimitService;
    private final TransactionMapper transactionMapper;
    private final UserService userService;

    @Override
    public TransactionData deposit(Long cardId, BigDecimal amount) {
        Card card = cardService.findById(cardId);
        cardService.verifyCardOwnership(card);
        cardService.checkCardStatus(card);

        Transaction transaction = this.createTransaction(card, DEPOSIT, amount);

        card.setBalance(card.getBalance().add(amount));

        transactionRepository.save(transaction);
        cardService.updateCard(card);

        return transactionMapper.map(transaction);
    }

    @Override
    public TransactionData withdraw(Long cardId, BigDecimal amount) {
        Card card = cardService.findById(cardId);
        cardService.verifyCardOwnership(card);
        cardService.checkCardStatus(card);

        this.checkBalanceAfterTransaction(card, amount);

        cardLimitService.checkLimit(card, amount, LimitType.DAILY_WITHDRAWAL);
        cardLimitService.checkLimit(card, amount, LimitType.MONTHLY_WITHDRAWAL);

        Transaction transaction = this.createTransaction(card, WITHDRAW, amount);

        card.setBalance(card.getBalance().subtract(amount));

        transactionRepository.save(transaction);
        cardService.updateCard(card);

        return transactionMapper.map(transaction);
    }

    @Override
    public List<TransactionData> transferBetweenOwnCards(Long debitCardId, Long transferCardId, BigDecimal amount) {
        TransactionData withdrawTransaction = this.withdraw(debitCardId, amount);
        TransactionData depositTransaction = this.deposit(transferCardId, amount);

        return List.of(withdrawTransaction, depositTransaction);
    }

    @Override
    public TransactionData getTransactionById(long id) {
        Transaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        return transactionMapper.map(transaction);
    }

    @Override
    public Page<TransactionData> getAllUserTransaction(int page, int size) {
        long currentUserId = userService.currentUser().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Transaction> transactions = transactionRepository.findAllByUserId(currentUserId, pageable);
        return transactionMapper.map(transactions);
    }

    @Override
    public Page<TransactionData> getAllCardTransaction(long cardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Transaction> transactions = transactionRepository.findAllByCardId(cardId, pageable);
        return transactionMapper.map(transactions);
    }

    @Override
    public Page<TransactionData> getAllUserTransactionForDate(LocalDate date, int page, int size) {
        long currentUserId = userService.currentUser().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Transaction> transactions = transactionRepository.findAllByUserIdOrderByTimestamp(currentUserId, date, pageable);
        return transactionMapper.map(transactions);
    }

    @Override
    public Page<TransactionData> getAllCardTransactionForDate(long cardId, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Transaction> transactions = transactionRepository.findAllByCardIdOrderByTimestamp(cardId, date, pageable);
        return transactionMapper.map(transactions);
    }

    private Transaction createTransaction(Card card, TransactionType transactionType, BigDecimal amount) {
        return new Transaction()
                .setCard(card)
                .setAmount(amount)
                .setTimestamp(LocalDateTime.now())
                .setType(transactionType)
                .setDescription(transactionType.getDescription());
    }

    private void checkBalanceAfterTransaction(Card card, BigDecimal amount) {
        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
    }
}
