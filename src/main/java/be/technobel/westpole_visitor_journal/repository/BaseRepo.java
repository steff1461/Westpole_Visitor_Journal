package be.technobel.westpole_visitor_journal.repository;

import be.technobel.westpole_visitor_journal.utils.UtilsHiber;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class BaseRepo<T> implements IRepository<T> {

    private final Class<T> type;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<T> query;
    private Root<T> root;

    public BaseRepo(Class<T> type) {

        this.type = type;
    }

    @Override
    public void merge(T entity) {

        EntityManager manager = UtilsHiber.entityManagerInstance();
        manager.getTransaction().begin();
        manager.merge(entity);
        manager.getTransaction().commit();
    }

    @Override
    public void create(T entity) {

        EntityManager manager = UtilsHiber.entityManagerInstance();
        manager.getTransaction().begin();
        manager.persist(entity);
        manager.getTransaction().commit();
    }

    @Override
    public List<T> findAll() {

        EntityManager manager = UtilsHiber.entityManagerInstance();
        criteriaBuilder = manager.getCriteriaBuilder();
        query = criteriaBuilder.createQuery(type);
        root = query.from(type);
        query.select(root);

        manager.getTransaction().begin();
        List<T> visitors = manager.createQuery(query).getResultList();
        manager.getTransaction().commit();
        return visitors;
    }

    @Override
    public T findOne(int id) {

        EntityManager manager = UtilsHiber.entityManagerInstance();

        query.select(root)
                .where(criteriaBuilder.equal(root.get("id"), "" + id));


        return manager
                .createQuery(query)
                .getSingleResult();
    }

    @Override
    public void delete(int id) {

        EntityManager manager = UtilsHiber.entityManagerInstance();
        manager.getTransaction().begin();
        T tempEntity = findOne(id);
        if (manager.contains(tempEntity)) manager.remove(tempEntity);
        manager.getTransaction().commit();
    }

    @Override
    public void update(T entity) {

        EntityManager manager = UtilsHiber.entityManagerInstance();
        manager.getTransaction().begin();
        manager.merge(entity);
        manager.getTransaction().commit();
    }
}
