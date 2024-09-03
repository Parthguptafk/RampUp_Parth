package org.Flipkart;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.Flipkart.core.User;
import org.Flipkart.dao.UserDao;
import org.Flipkart.exception.InvalidRequestException;
import org.Flipkart.exception.MissingDataException;
import org.Flipkart.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUserSuccess() throws MissingDataException {
        User user = new User();
        when(userDao.addUser(user)).thenReturn(user);

        User result = userService.addUser(user);
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testAddUserFailure() {
        User user = new User();
        when(userDao.addUser(user)).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(MissingDataException.class, () -> {
            userService.addUser(user);
        });
        assertTrue(exception.getMessage().contains("Failed to add user"));
    }

    @Test
    void testGetUserByIdSuccess() throws InvalidRequestException {
        Long userId = 1L;
        User user = new User();
        when(userDao.getUserById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(userId);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetUserByIdNotFound() {
        Long userId = 1L;
        when(userDao.getUserById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(MissingDataException.class, () -> {
            userService.getUserById(userId).orElseThrow(() -> new MissingDataException("User not found"));
        });
        assertTrue(exception.getMessage().contains("User not found"));
    }
}

