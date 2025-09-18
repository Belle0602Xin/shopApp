package com.shop.repository;

import com.shop.entity.Product;
import com.shop.entity.User;
import com.shop.entity.Watchlist;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class WatchlistRepository extends BaseRepository<Watchlist, Long> {

    public WatchlistRepository() {
        super(Watchlist.class);
    }

    public List<Watchlist> findByUserId(Long userId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Watchlist> query = cb.createQuery(Watchlist.class);
        Root<Watchlist> root = query.from(Watchlist.class);

        query.select(root)
             .where(cb.equal(root.get("user").get("id"), userId))
             .orderBy(cb.desc(root.get("addedAt")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public List<Watchlist> findAvailableWatchlistByUserId(Long userId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Watchlist> query = cb.createQuery(Watchlist.class);
        Root<Watchlist> root = query.from(Watchlist.class);
        Join<Watchlist, Product> productJoin = root.join("product");

        query.select(root)
             .where(cb.and(
                 cb.equal(root.get("user").get("id"), userId),
                 cb.gt(productJoin.get("inventory"), 0)
             ))
             .orderBy(cb.desc(root.get("addedAt")));

        return getCurrentSession().createQuery(query).getResultList();
    }

    public Optional<Watchlist> findByUserIdAndProductId(Long userId, Long productId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Watchlist> query = cb.createQuery(Watchlist.class);
        Root<Watchlist> root = query.from(Watchlist.class);

        query.select(root)
             .where(cb.and(
                 cb.equal(root.get("user").get("id"), userId),
                 cb.equal(root.get("product").get("id"), productId)
             ));

        return getCurrentSession().createQuery(query)
                .getResultStream()
                .findFirst();
    }

    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Watchlist> root = query.from(Watchlist.class);

        query.select(cb.count(root))
             .where(cb.and(
                 cb.equal(root.get("user").get("id"), userId),
                 cb.equal(root.get("product").get("id"), productId)
             ));

        return getCurrentSession().createQuery(query).getSingleResult() > 0;
    }

    public void deleteByUserIdAndProductId(Long userId, Long productId) {
        Optional<Watchlist> watchlist = findByUserIdAndProductId(userId, productId);
        watchlist.ifPresent(this::delete);
    }

    public long countByUserId(Long userId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Watchlist> root = query.from(Watchlist.class);

        query.select(cb.count(root))
             .where(cb.equal(root.get("user").get("id"), userId));

        return getCurrentSession().createQuery(query).getSingleResult();
    }

    public List<Product> findWatchedProductsByUserId(Long userId) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Watchlist> root = query.from(Watchlist.class);
        Join<Watchlist, Product> productJoin = root.join("product");

        query.select(productJoin)
             .where(cb.equal(root.get("user").get("id"), userId))
             .orderBy(cb.desc(root.get("addedAt")));

        return getCurrentSession().createQuery(query).getResultList();
    }
}