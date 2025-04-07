package com.gdmn.card_manager.service;

import com.gdmn.card_manager.dto.TransactionData;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransactionData deposit(Long cardId, BigDecimal amount);

    TransactionData withdraw(Long cardId, BigDecimal amount);

    List<TransactionData> transferBetweenOwnCards(Long cardId, Long transferCardId, BigDecimal amount);

    TransactionData getTransactionById(long id);

    Page<TransactionData> getAllUserTransaction(int page, int size);

    Page<TransactionData> getAllCardTransaction(long cardId, int page, int size);

    Page<TransactionData> getAllUserTransactionForDate(LocalDate date, int page, int size);

    Page<TransactionData> getAllCardTransactionForDate(long cardId, LocalDate date, int page, int size);
}
