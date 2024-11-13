package org.intro.retojfxhib.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.intro.retojfxhib.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class UserDAO implements DAO<User> {
    private final SessionFactory sessionFactory;

    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User findById(Long id) {
        User u;
        try(var session = sessionFactory.openSession()) {
            u = session.get(User.class, id);
        }
        return u;
    }

    @Override
    public void save(User user) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {}

    @Override
    public void delete(User user) {}

    public User validateUser(String email, String password) {
        User u;
        try(Session session =  sessionFactory.openSession()) {
            u = session
                    .createQuery("select u from User u where email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResultOrNull();
            if(!BCrypt.checkpw(password, u.getPassword()))
                u = null;
        }
        return u;
    }
}
