package com.gdmn.card_manager.repository;

import com.gdmn.card_manager.enums.UserRequestStatus;
import com.gdmn.card_manager.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    List<UserRequest> findByStatus(UserRequestStatus status);
}
