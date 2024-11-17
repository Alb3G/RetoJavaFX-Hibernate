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

    /**
     * FindAll devuelve una lista de todos los objetos User existentes en la DB.     *
     * @return Lista de objetos User
     * @throws UnsupportedOperationException método no implementado
     */
    @Override
    public List<User> findAll() {
        throw new UnsupportedOperationException("No implementation for method findAll();");
    }

    /**
     * Método para recuperar un objeto User específico en función de su id.
     *
     * @param id Identificador de User
     * @return objeto User
     */
    @Override
    public User findById(Long id) {
        User u;
        try(var session = sessionFactory.openSession()) {
            u = session.get(User.class, id);
        }
        return u;
    }

    /**
     * Método para guardar un objeto User en la DB.
     *
     * @param user objeto User a guardar en la DB.
     */
    @Override
    public void save(User user) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    /**
     * Método para modificar un objeto User ya existente en la DB.
     *
     * @param user objeto User a modificar en la DB.
     */
    @Override
    public void update(User user) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    /**
     * Método para eliminar un objeto User en la DB.
     *
     * @param user objeto User a eliminar en la DB.
     */
    @Override
    public void delete(User user) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        }
    }

    /**
     * Método para validar un Usuario donde según si existe un determinado email
     * recibido como parámetro recuperamos el objeto entero y si la contraseña que viene cifrada
     * de la base de datos coincide con la recibida como parámetro resultaría ser un usuario válido.
     * @param email Del User
     * @param password Del User
     * @return User en caso de que exista el email en la Db y de que la contraseña sea válida.
     */
    public User validateUser(String email, String password) {
        User u;
        try(Session session =  sessionFactory.openSession()) {
            u = session
                    .createQuery("select u from User u where email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResultOrNull();
            if(!BCrypt.checkpw(password, u.getPassword()))
                u = null;
        } catch (Exception e) {
            u = null;
        }
        return u;
    }
}
