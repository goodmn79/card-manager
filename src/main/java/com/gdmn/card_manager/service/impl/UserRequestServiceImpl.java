package com.gdmn.card_manager.service.impl;

import com.gdmn.card_manager.enums.Role;
import com.gdmn.card_manager.enums.UserRequestStatus;
import com.gdmn.card_manager.exseption.AccessDeniedException;
import com.gdmn.card_manager.exseption.UserRequestNotFoundException;
import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.model.UserRequest;
import com.gdmn.card_manager.repository.UserRequestRepository;
import com.gdmn.card_manager.service.CardService;
import com.gdmn.card_manager.service.UserRequestService;
import com.gdmn.card_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {
    private final UserRequestRepository userRequestRepository;
    private final UserService userService;
    private final CardService cardService;

    @Override
    public void changeStatusRequest(long cardId) {
        User requester = userService.currentUser();
        Card card = cardService.findById(cardId);
        if (!requester.equals(card.getOwner())) {
            throw new AccessDeniedException("You do not have permission to change the status of the card");
        }
        UserRequest request = new UserRequest()
                .setCard(card)
                .setRequester(requester)
                .setRequestDate(LocalDateTime.now())
                .setStatus(UserRequestStatus.PENDING);
        userRequestRepository.save(request);
    }

    @Override
    @Transactional
    public void changeStatusProcess(Long requestId, boolean approve) {
        User admin = userService.currentUser();
        if (!admin.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AccessDeniedException("Only admin can process requests");
        }

        UserRequest request = userRequestRepository.findById(requestId)
                .orElseThrow(() -> new UserRequestNotFoundException("Request not found"));

        if (approve) {
            long cardId = request.getCard().getId();
            cardService.changeStatus(cardId);

            request.setStatus(UserRequestStatus.APPROVED);
        } else {
            request.setStatus(UserRequestStatus.REJECTED);
        }

        request.setProcessDate(LocalDateTime.now());
        userRequestRepository.save(request);
    }

    @Override
    public List<UserRequest> getPendingRequests() {
        List<UserRequest> requests = userRequestRepository.findByStatus(UserRequestStatus.PENDING);
        if (requests.isEmpty()) {
            throw new UserRequestNotFoundException("Requests not found");
        }
        return requests;
    }
}
