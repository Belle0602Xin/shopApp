package com.shop.repository;

import com.shop.entity.Order;
import com.shop.entity.User;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepository extends BaseRepository<Order, Long> {

    public OrderRepository() {
        super(Order.class);
    }

    public List<Order> findByUser(User user) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        query.select(root)
             .where(cb.equal(root.get("user"), user))
             .orderBy(cb.desc(root.get("orderTime")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<Order> findByUserId(Long userId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        query.select(root)
             .where(cb.equal(root.get("user").get("id"), userId))
             .orderBy(cb.desc(root.get("orderTime")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<Order> findByStatus(Order.OrderStatus status) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        query.select(root)
             .where(cb.equal(root.get("status"), status))
             .orderBy(cb.desc(root.get("orderTime")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<Order> findOrdersWithPagination(int page, int size) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        query.select(root)
             .orderBy(cb.desc(root.get("orderTime")));

        return getCurrentSession().createQuery(query)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Order> findOrdersByUserWithStatus(Long userId, Order.OrderStatus status) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        query.select(root)
             .where(cb.and(
                 cb.equal(root.get("user").get("id"), userId),
                 cb.equal(root.get("status"), status)
             ))
             .orderBy(cb.desc(root.get("orderTime")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<Order> findRecentCompletedOrdersByUser(Long userId, int limit) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        query.select(root)
             .where(cb.and(
                 cb.equal(root.get("user").get("id"), userId),
                 cb.equal(root.get("status"), Order.OrderStatus.COMPLETED)
             ))
             .orderBy(cb.desc(root.get("orderTime")));

        return getCurrentSession().createQuery(query)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        query.select(root)
             .where(cb.between(root.get("orderTime"), startDate, endDate))
             .orderBy(cb.desc(root.get("orderTime")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public long countByStatus(Order.OrderStatus status) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Order> root = query.from(Order.class);

        query.select(cb.count(root))
             .where(cb.equal(root.get("status"), status));

        return getCurrentSession().createQuery(query).getSingleResult();
    }
}