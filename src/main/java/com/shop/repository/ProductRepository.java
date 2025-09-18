package com.shop.repository;

import com.shop.entity.Product;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductRepository extends BaseRepository<Product, Long> {

    public ProductRepository() {
        super(Product.class);
    }

    public List<Product> findAvailableProducts() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        query.select(root)
             .where(cb.gt(root.get("inventory"), 0))
             .orderBy(cb.asc(root.get("description")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<Product> findByDescriptionContaining(String keyword) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        Predicate descriptionPredicate = cb.like(
            cb.lower(root.get("description")),
            "%" + keyword.toLowerCase() + "%"
        );

        query.select(root)
             .where(descriptionPredicate)
             .orderBy(cb.asc(root.get("description")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<Product> findAvailableProductsByDescriptionContaining(String keyword) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        Predicate inventoryPredicate = cb.gt(root.get("inventory"), 0);
        Predicate descriptionPredicate = cb.like(
            cb.lower(root.get("description")),
            "%" + keyword.toLowerCase() + "%"
        );

        query.select(root)
             .where(cb.and(inventoryPredicate, descriptionPredicate))
             .orderBy(cb.asc(root.get("description")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public long countAvailableProducts() {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Product> root = query.from(Product.class);

        query.select(cb.count(root))
             .where(cb.gt(root.get("inventory"), 0));

        return getCurrentSession().createQuery(query).getSingleResult();
    }

    public List<Product> findLowStockProducts(int threshold) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        query.select(root)
             .where(cb.and(
                 cb.ge(root.get("inventory"), 0),
                 cb.le(root.get("inventory"), threshold)
             ))
             .orderBy(cb.asc(root.get("inventory")));

        return getCurrentSession().createQuery(query).getResultList();
    }
}