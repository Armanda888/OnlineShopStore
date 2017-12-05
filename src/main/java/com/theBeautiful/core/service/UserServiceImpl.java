package com.theBeautiful.core.service;

import com.theBeautiful.cassandra.dao.UserDao;
import com.theBeautiful.cassandra.util.JiamiString;
import com.theBeautiful.model.Address;
import com.theBeautiful.model.User;

import javax.inject.Inject;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by jiaoli on 10/7/17
 */
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Inject
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public void signUp(User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new InvalidParameterException("User info invalid to signup");
        }
        userDao.upsert(user);
    }

    public boolean login(User user, String password) {
        return userDao.login(user, password);
    }

    public User getByEmail(String email) {
        User user = userDao.getByEmail(email);
        return user;
    }

    public User getById(String userId) {
        User user = userDao.getById(userId);
        return user;
    }

    public void removeUserByEmail(User user) {
        userDao.removeByEmail(user);
    }

    public User addAddress(User user, Address address) {
        return this.userDao.addAddress(user, address);
    }

    public void removeAddress(User user, Address address) {
        this.userDao.removeAddress(user, address);
    }
}
