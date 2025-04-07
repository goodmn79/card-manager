package com.gdmn.card_manager.repository;

import com.gdmn.card_manager.enums.TransactionType;
import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCardAndTypeAndTimestampAfter(
            Card card, TransactionType type, LocalDateTime timestamp);

    Page<Transaction> findAllByUserId(long userId, Pageable pageable);

    Page<Transaction> findAllByCardId(long cardId, Pageable pageable);

    @Query(value = "SELECT * FROM transactions t WHERE t.user_id = :userId AND date(t.timestamp) = :date ORDER BY t.timestamp DESC;", nativeQuery = true)
    Page<Transaction> findAllByUserIdOrderByTimestamp(long userId, LocalDate date, Pageable pageable);

    @Query(value = "SELECT * FROM transactions t WHERE t.card_id = :cardId AND date(t.timestamp) = :date ORDER BY t.timestamp DESC;;", nativeQuery = true)
    Page<Transaction> findAllByCardIdOrderByTimestamp(long cardId, LocalDate date, Pageable pageable);
}
