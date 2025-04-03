package com.gdmn.card_manager.model;

import com.gdmn.card_manager.converter.CardNumberEncryptor;
import com.gdmn.card_manager.enums.CardStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cards")
@Data
@Accessors(chain = true)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_number")
    @Convert(converter = CardNumberEncryptor.class)
    private String cardNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "card", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<CardLimit> limits;
}
