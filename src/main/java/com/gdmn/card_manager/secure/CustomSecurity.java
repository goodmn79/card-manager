package com.gdmn.card_manager.secure;

import com.gdmn.card_manager.model.Card;
import com.gdmn.card_manager.model.User;
import com.gdmn.card_manager.service.CardService;
import com.gdmn.card_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("customSecurity")
@RequiredArgsConstructor
public class CustomSecurity {
    private final UserService userService;
    private final CardService cardService;

    public boolean isDataOwnerOrAdmin(long userId) {
        User currentUser = userService.currentUser();
        String role = currentUser.getRole().name();
        return currentUser.getId() == userId || role.equals("ROLE_ADMIN");
    }

    public boolean isCardOwnerOrAdmin(long cardId) {
        Card card = cardService.findById(cardId);
        User owner = card.getOwner();
        User currentUser = userService.currentUser();
        String role = currentUser.getRole().name();
        return Objects.equals(currentUser.getId(), owner.getId()) || role.equals("ROLE_ADMIN");
    }
}
