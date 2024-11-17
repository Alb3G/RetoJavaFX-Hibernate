package org.intro.retojfxhib.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.intro.retojfxhib.dto.CopyDTO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.MovieCopy;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de realizar todas las operaciones de lectura y escritura
 * en la DB relacionadas con el objeto Movie.
 *
 * @author Alberto Guzman
 */
public class MovieDAO implements DAO<Movie> {
    private final SessionFactory sessionFactory;

    /**
     * Constructor de la clase donde realizamos la inyección de dependencias con el
     * Singleton de la conexion con Hibernate.
     * @param sessionFactory Singleton Connection Obj
     */
    public MovieDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * FindAll devuelve una lista de todos los objetos Movie existentes en la DB.
     *
     * @return Lista de objetos Movie
     */
    @Override
    public List<Movie> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Movie", Movie.class).list();
        }
    }

    /**
     * Método para recuperar un objeto Movie específico en función de su id.
     *
     * @param id Identificador de Movie
     * @return objeto Movie
     */
    @Override
    public Movie findById(Long id) {
        Movie movie;
        try(var session = sessionFactory.openSession()) {
            movie = session.get(Movie.class, id);
        }
        return movie;
    }

    /**
     * Método para guardar un objeto Movie en la DB.
     *
     * @param movie objeto Movie a guardar en la DB.
     */
    @Override
    public void save(Movie movie) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(movie);
            session.getTransaction().commit();
        }
    }

    /**
     * Método para modificar las propiedades un objeto Movie en la DB.
     *
     * @param movie objeto a guardar en la DB.
     */
    @Override
    public void update(Movie movie) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(movie);
            session.getTransaction().commit();
        }
    }

    /**
     * Método para eliminar un objeto Movie de la DB.
     * @param movie objeto a eliminar de la DB.
     */
    @Override
    public void delete(Movie movie) {}

    /**
     * Método para recuperar los diferentes géneros existentes en la DB.
     * @return Lista de Strings con los géneros.
     */
    public List<String> getGenres() {
        List<String> genres;
        try(Session session = sessionFactory.openSession()) {
            genres = session.createQuery("select distinct(m.genre) from Movie m", String.class).list();
        } catch (Exception e) {
            genres = new ArrayList<>(0);
        }
        return genres;
    }

    /**
     * Método para recuperar los diferentes directores existentes en la DB.
     * @return Lista de Strings con los directores.
     */
    public List<String> getDirectors() {
        List<String> directors;
        try(Session session = sessionFactory.openSession()) {
            directors = session.createQuery("select distinct(m.director) from Movie m", String.class).list();
        } catch (Exception e) {
            directors = new ArrayList<>(0);
        }
        return directors;
    }

    /**
     * Método para recuperar todas las copias(MovieCopy) que tiene un usuario y así construir
     * tantos objetos DTOs como copias tenga el usuario para después mostrarl@s en una tabla.
     *
     * @param userId Id del usuario.
     * @return Lista de objetos CopyDTO
     */
    public List<CopyDTO> getDtoObjOfUser(Long userId) {
        List<CopyDTO> dtos = new ArrayList<>();
        try(var session = sessionFactory.openSession()) {
            Query<MovieCopy> query = session.createQuery("select mc from MovieCopy mc where user.id = :id", MovieCopy.class);
            query.setParameter("id", userId);
            var copies = query.list();
            for(MovieCopy copy : copies) {
                dtos.add(new CopyDTO(
                        copy,
                        copy.getMovieCondition(),
                        copy.getPlatform()
                ));
            }
        }
        return dtos;
    }
}
