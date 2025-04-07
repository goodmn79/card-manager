package com.gdmn.card_manager.model;

import com.gdmn.card_manager.enums.UserRequestStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "card_requests")
@Data
@Accessors(chain = true)
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requester;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "process_date")
    private LocalDateTime processDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserRequestStatus status;
}
