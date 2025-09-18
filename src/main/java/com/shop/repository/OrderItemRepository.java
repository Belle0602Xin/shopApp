package com.shop.repository;

import com.shop.entity.OrderItem;
import com.shop.entity.Product;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class OrderItemRepository extends BaseRepository<OrderItem, Long> {

    public OrderItemRepository() {
        super(OrderItem.class);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<OrderItem> query = cb.createQuery(OrderItem.class);
        Root<OrderItem> root = query.from(OrderItem.class);

        query.select(root)
             .where(cb.equal(root.get("order").get("id"), orderId));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<OrderItem> findByProductId(Long productId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<OrderItem> query = cb.createQuery(OrderItem.class);
        Root<OrderItem> root = query.from(OrderItem.class);

        query.select(root)
             .where(cb.equal(root.get("product").get("id"), productId));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<Object[]> findTopSellingProducts(int limit) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<OrderItem> root = query.from(OrderItem.class);
        Join<OrderItem, Product> productJoin = root.join("product");

        query.multiselect(
                productJoin,
                cb.sum(root.get("quantity")).alias("totalQuantity")
             )
             .where(cb.notEqual(
                 root.get("order").get("status"),
                 com.shop.entity.Order.OrderStatus.CANCELED
             ))
             .groupBy(productJoin.get("id"))
             .orderBy(cb.desc(cb.sum(root.get("quantity"))));

        return getCurrentSession().createQuery(query)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Object[]> findMostProfitableProducts() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<OrderItem> root = query.from(OrderItem.class);
        Join<OrderItem, Product> productJoin = root.join("product");

        Expression<BigDecimal> profitPerItem = cb.diff(
            root.get("unitPrice"),
            productJoin.get("wholesalePrice")
        );
        Expression<BigDecimal> totalProfit = cb.prod(
            profitPerItem,
            root.get("quantity")
        );

        query.multiselect(
                productJoin,
                cb.sum(totalProfit).alias("totalProfit")
             )
             .where(cb.equal(
                 root.get("order").get("status"),
                 com.shop.entity.Order.OrderStatus.COMPLETED
             ))
             .groupBy(productJoin.get("id"))
             .orderBy(cb.desc(cb.sum(totalProfit)));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public BigDecimal calculateTotalSales() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class);
        Root<OrderItem> root = query.from(OrderItem.class);

        query.select(cb.sum(root.get("subtotal")))
             .where(cb.equal(
                 root.get("order").get("status"),
                 com.shop.entity.Order.OrderStatus.COMPLETED
             ));

        BigDecimal result = getCurrentSession().createQuery(query).getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }

    public Long calculateTotalSoldQuantity() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<OrderItem> root = query.from(OrderItem.class);

        query.select(cb.sum(root.get("quantity")))
             .where(cb.notEqual(
                 root.get("order").get("status"),
                 com.shop.entity.Order.OrderStatus.CANCELED
             ));

        Long result = getCurrentSession().createQuery(query).getSingleResult();
        return result != null ? result : 0L;
    }

    public List<OrderItem> findRecentPurchasesByUser(Long userId, int limit) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<OrderItem> query = cb.createQuery(OrderItem.class);
        Root<OrderItem> root = query.from(OrderItem.class);

        query.select(root)
             .where(cb.and(
                 cb.equal(root.get("order").get("user").get("id"), userId),
                 cb.equal(root.get("order").get("status"), com.shop.entity.Order.OrderStatus.COMPLETED)
             ))
             .orderBy(cb.desc(root.get("order").get("orderTime")));

        return getCurrentSession().createQuery(query)
                .setMaxResults(limit)
                .getResultList();
    }
}