package com.theBeautiful.cassandra.dao;

import com.theBeautiful.model.Address;
import com.theBeautiful.model.User;

import java.util.List;

/**
 * Created by jiaoli on 9/30/17
 */
public interface UserDao {

    List<User> getUsers();

    /*To insert or update user item to db */
    User upsert(User user);

    boolean login(User user, String password);

    /* Get user by email */
    User getByEmail(String email);

    User getById(String userId);

    /* Add address to user information */
    User addAddress(User user, Address address);

    User removeAddress(User user, Address address);

    /* Remove user */
    void removeByEmail(User user);



}
