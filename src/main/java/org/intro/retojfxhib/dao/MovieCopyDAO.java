package org.intro.retojfxhib.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.intro.retojfxhib.models.MovieCopy;

import java.util.ArrayList;
import java.util.List;

public class MovieCopyDAO implements DAO<MovieCopy> {
    private SessionFactory sessionFactory;

    public MovieCopyDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<MovieCopy> findAll() {
        return List.of();
    }

    @Override
    public MovieCopy findById(Long id) {
        return null;
    }

    @Override
    public void save(MovieCopy movieCopy) {
        try(var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(movieCopy);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(MovieCopy movieCopy) {

    }

    @Override
    public void delete(MovieCopy movieCopy) {

    }

    public List<String> getCopiesPlatform() {
        List<String> res;
        try(var session = sessionFactory.openSession()) {
            res = session.createQuery("select distinct(mc.platform) from MovieCopy mc", String.class).list();
        } catch (Exception e) {
            res = new ArrayList<>();
        }
        return res;
    }

    public Integer getNumOfPlatforms() {
        Integer res;
        try(var session = sessionFactory.openSession()) {
            Query<Integer> query = session.createQuery("select count(distinct(mc.platform)) from MovieCopy mc", Integer.class);
            res = query.getSingleResultOrNull();
        }
        return res;
    }

    public List<String> getCopiesCondition() {
        List<String> res;
        try(var session = sessionFactory.openSession()) {
            res = session.createQuery("select distinct(mc.movieCondition) from MovieCopy mc", String.class).list();
        } catch (Exception e) {
            res = new ArrayList<>();
        }
        return res;
    }

    public Long getNumOfConditions() {
        Long res;
        try(var session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("select count(distinct(mc.movieCondition)) from MovieCopy mc", Long.class);
            res = query.getSingleResultOrNull();
        }
        return res;
    }

}
