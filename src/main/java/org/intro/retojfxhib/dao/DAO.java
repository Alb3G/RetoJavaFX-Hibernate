package org.intro.retojfxhib.dao;

import java.util.List;

public interface DAO<T>{
    List<T> findAll();
    T findById(Long id);
    void save(T t);
    void update(T t);
    void delete(T t);
}
