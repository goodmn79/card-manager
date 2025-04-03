package com.gdmn.card_manager.repository;

import com.gdmn.card_manager.enums.LimitType;
import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.CardLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardLimitRepository extends JpaRepository<CardLimit, Long> {
    Optional<CardLimit> findByCardAndLimitType(Card card, LimitType limitType);
}
