package org.Flipkart.service;

import org.Flipkart.core.User;
import org.Flipkart.dao.UserDao;
import org.Flipkart.exception.InvalidRequestException;
import org.Flipkart.exception.MissingDataException;
import java.util.Optional;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> getUserById(Long id) throws InvalidRequestException {
        try {
            return userDao.getUserById(id);
        } catch (Exception e) {
            throw new InvalidRequestException("Failed to retrieve user with ID: " + id);
        }
    }

    public User addUser(User user) throws  MissingDataException {
        try {
            return userDao.addUser(user);
        } catch (Exception e) {
            throw new MissingDataException("Failed to add user");
        }
    }
}
