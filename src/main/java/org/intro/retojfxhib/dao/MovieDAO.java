package org.intro.retojfxhib.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.intro.retojfxhib.dto.CopyDTO;
import org.intro.retojfxhib.models.Movie;
import org.intro.retojfxhib.models.MovieCopy;

import java.util.ArrayList;
import java.util.List;

public class MovieDAO implements DAO<Movie> {
    private final SessionFactory sessionFactory;

    public MovieDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Movie> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Movie", Movie.class).list();
        }
    }

    @Override
    public Movie findById(Long id) {
        Movie movie;
        try(var session = sessionFactory.openSession()) {
            movie = session.get(Movie.class, id);
        }
        return movie;
    }

    @Override
    public void save(Movie movie) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(movie);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Movie movie) {}

    @Override
    public void delete(Movie movie) {}

    public List<String> getGenres() {
        List<String> genres;
        try(Session session = sessionFactory.openSession()) {
            genres = session.createQuery("select distinct(m.genre) from Movie m", String.class).list();
        } catch (Exception e) {
            genres = new ArrayList<>(0);
        }
        return genres;
    }

    public List<String> getDirectors() {
        List<String> directors;
        try(Session session = sessionFactory.openSession()) {
            directors = session.createQuery("select distinct(m.director) from Movie m", String.class).list();
        } catch (Exception e) {
            directors = new ArrayList<>(0);
        }
        return directors;
    }

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
