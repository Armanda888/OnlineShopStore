package com.theBeautiful.core.service;

import com.theBeautiful.model.Address;
import com.theBeautiful.model.User;

import java.util.List;

/**
 * Created by jiaoli on 10/7/17
 */
public interface UserService {
    void signUp(User user);

    List<User> getUsers();

    boolean login(User user, String password);

    User getByEmail(String email);

    User getById(String userId);

    User addAddress(User user, Address address);

    void removeAddress(User user, Address address);

    void removeUserByEmail(User user);
}
