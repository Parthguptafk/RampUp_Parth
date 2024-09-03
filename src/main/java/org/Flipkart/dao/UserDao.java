package org.Flipkart.dao;

import org.Flipkart.core.User;
import org.hibernate.SessionFactory;
import io.dropwizard.hibernate.AbstractDAO;
import java.util.Optional;

public class UserDao extends AbstractDAO<User> {

    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);

    }

    public Optional<User> getUserById(Long id) {

        return Optional.ofNullable(get(id));
    }


    public User addUser(User user) {

        this.persist(user);
        return user;
    }

}
