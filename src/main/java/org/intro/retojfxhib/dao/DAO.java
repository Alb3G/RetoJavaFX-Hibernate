package org.intro.retojfxhib.dao;

import java.util.List;

/**
 * @author Alberto Guzman
 * @param <T> Parámetro a implementar por la interfaz
 */
public interface DAO<T>{
    List<T> findAll();
    T findById(Long id);
    void save(T t);
    void update(T t);
    void delete(T t);
}
