package com.gdmn.card_manager.repository;

import com.gdmn.card_manager.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query(value = "SELECT c FROM cards c WHERE c.user_id = ? ORDER BY c.expiry_date", nativeQuery = true)
    Page<Card> findUserCardsSortedByExpiryDate(Long userId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE cards SET status = 'EXPIRED' WHERE expiry_date = CURRENT_DATE AND status <> 'EXPIRED'", nativeQuery = true)
    void updateExpired();
}
