package com.shop.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public abstract class BaseRepository<T, ID extends Serializable> {

    @Autowired
    protected SessionFactory sessionFactory;

    private final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public T save(T entity) {
        getCurrentSession().persist(entity);
        return entity;
    }

    public T update(T entity) {
        return getCurrentSession().merge(entity);
    }

    public void delete(T entity) {
        getCurrentSession().remove(entity);
    }

    public void deleteById(ID id) {
        T entity = findById(id).orElse(null);
        if (entity != null) {
            delete(entity);
        }
    }

    public Optional<T> findById(ID id) {
        T entity = getCurrentSession().get(entityClass, id);
        return Optional.ofNullable(entity);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.select(root);
        return getCurrentSession().createQuery(query).getResultList();
    }

    public long count() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<T> root = query.from(entityClass);
        query.select(cb.count(root));
        return getCurrentSession().createQuery(query).getSingleResult();
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }
}