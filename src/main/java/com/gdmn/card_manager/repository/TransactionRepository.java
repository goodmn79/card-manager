package com.gdmn.card_manager.repository;

import com.gdmn.card_manager.enums.TransactionType;
import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardAndTypeAndTimestampAfter(Card card, TransactionType transactionTypeForLimitType, LocalDateTime startDate);
}
