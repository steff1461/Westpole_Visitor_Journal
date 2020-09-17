package be.technobel.westpole_visitor_journal.repository;

import java.util.List;

public interface IRepository<T> {

    void merge (T entity);

    void create(T entity);

    List<T> findAll();

    T findOne(int id);

    void delete(int id);

    void update(T entity);

}
