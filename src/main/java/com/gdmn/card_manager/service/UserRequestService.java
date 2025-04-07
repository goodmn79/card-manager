package com.gdmn.card_manager.service;

import com.gdmn.card_manager.model.UserRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRequestService {
    void changeStatusRequest(long cardId);

    @Transactional
    void changeStatusProcess(Long requestId, boolean approve);

    List<UserRequest> getPendingRequests();
}
