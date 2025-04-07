package com.gdmn.card_manager.service;

import com.gdmn.card_manager.dto.Register;
import com.gdmn.card_manager.dto.UserData;
import com.gdmn.card_manager.model.User;

public interface UserService {
    void addUser(Register register);

    void disable(long id);

    void enable(long id);

    User getById(long id);

    User currentUser();

    UserData getUserById(long id);

    UserData getCurrentUser();

    boolean existsByUsername(String username);

    User getByUsername(String username);

    void deleteById(long id);
}
    