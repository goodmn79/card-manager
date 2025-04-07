package com.gdmn.card_manager.model;

import com.gdmn.card_manager.enums.LimitType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "card_limits")
@Data
@Accessors(chain = true)
public class CardLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Enumerated(EnumType.STRING)
    @Column(name = "limit_type")
    private LimitType limitType;

    @Column(name = "reset_date")
    private LocalDate resetDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
