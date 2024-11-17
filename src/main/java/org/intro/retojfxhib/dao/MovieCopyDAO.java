package org.intro.retojfxhib.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.intro.retojfxhib.models.MovieCopy;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de realizar todas las operaciones de lectura y escritura
 * en la DB relacionadas con el objeto MovieCopy.
 *
 * @author Alberto Guzman
 */
public class MovieCopyDAO implements DAO<MovieCopy> {
    private SessionFactory sessionFactory;

    /**
     * Constructor de la clase donde realizamos la inyección de dependencias con el
     * Singleton de la conexion con Hibernate.
     * @param sessionFactory Singleton Connection Obj
     */
    public MovieCopyDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * FindAll devuelve una lista de todos los objetos MovieCopy
     * existentes en la DB.
     *
     * @return Lista de objetos MovieCopy
     * @throws UnsupportedOperationException método no implementado
     */
    @Override
    public List<MovieCopy> findAll() {
        throw new UnsupportedOperationException("No implementation for method findAll();");
    }

    /**
     * Método para recuperar un objeto MovieCopy específico en función de su id.
     *
     * @param id Identificador de MovieCopy
     * @return objeto MovieCopy
     * @throws UnsupportedOperationException método no implementado
     */
    @Override
    public MovieCopy findById(Long id) {
        throw new UnsupportedOperationException("No implementation for method findById(Long id);");
    }

    /**
     * Método para guardar un objeto MovieCopy en la DB.
     *
     * @param movieCopy objeto a guardar en la DB.
     */
    @Override
    public void save(MovieCopy movieCopy) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(movieCopy);
            session.getTransaction().commit();
        }
    }

    /**
     * Método para modificar los valores de un objeto MovieCopy ya existente en la DB.
     * @param movieCopy objeto a modificar en la DB.
     */
    @Override
    public void update(MovieCopy movieCopy) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(movieCopy);
            session.getTransaction().commit();
        }
    }

    /**
     * Método para eliminar un objeto MovieCopy de la DB.
     * @param movieCopy objeto a eliminar de la DB.
     */
    @Override
    public void delete(MovieCopy movieCopy) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(movieCopy);
            session.getTransaction().commit();
        }
    }

    /**
     * Método para recuperar todas las plataformas diferentes que están disponibles en la DB.
     * @return Lista de Strings con las distintas plataformas disponibles en la DB.
     */
    public List<String> getCopiesPlatform() {
        List<String> res;
        try(var session = sessionFactory.openSession()) {
            res = session.createQuery("select distinct(mc.platform) from MovieCopy mc", String.class).list();
        } catch (Exception e) {
            res = new ArrayList<>();
        }
        return res;
    }

    /**
     * Método para recuperar la cantidad de plataformas diferentes que están disponibles en la DB.
     * @return Integer -> Número de plataformas.
     */
    public Integer getNumOfPlatforms() {
        Integer res;
        try(var session = sessionFactory.openSession()) {
            Query<Integer> query = session.createQuery("select count(distinct(mc.platform)) from MovieCopy mc", Integer.class);
            res = query.getSingleResultOrNull();
        }
        return res;
    }

    /**
     * Método para recuperar los tipos de estados/condiciones diferentes que están disponibles en la DB.
     * @return Lista de Strings con los distintos estados disponibles en la DB.
     */
    public List<String> getCopiesCondition() {
        List<String> res;
        try(var session = sessionFactory.openSession()) {
            res = session.createQuery("select distinct(mc.movieCondition) from MovieCopy mc", String.class).list();
        } catch (Exception e) {
            res = new ArrayList<>();
        }
        return res;
    }

    /**
     * Método para recuperar la cantidad de estados/condiciones diferentes que están disponibles en la DB.
     * @return Integer -> Número de estados diferentes.
     */
    public Long getNumOfConditions() {
        Long res;
        try(var session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select count(distinct(mc.movieCondition)) from MovieCopy mc", Long.class);
            res = query.getSingleResultOrNull();
        }
        return res;
    }

}
