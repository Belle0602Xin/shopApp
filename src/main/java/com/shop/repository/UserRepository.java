package com.shop.repository;

import com.shop.entity.User;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User, Long> {

    public UserRepository() {
        super(User.class);
    }

    public Optional<User> findByUsername(String username) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root)
             .where(cb.equal(root.get("username"), username));

        return getCurrentSession().createQuery(query)
                .getResultStream()
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root)
             .where(cb.equal(root.get("email"), email));

        return getCurrentSession().createQuery(query)
                .getResultStream()
                .findFirst();
    }

    public boolean existsByUsername(String username) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        query.select(cb.count(root))
             .where(cb.equal(root.get("username"), username));

        return getCurrentSession().createQuery(query).getSingleResult() > 0;
    }

    public boolean existsByEmail(String email) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        query.select(cb.count(root))
             .where(cb.equal(root.get("email"), email));

        return getCurrentSession().createQuery(query).getSingleResult() > 0;
    }

    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root)
             .where(cb.or(
                 cb.equal(root.get("username"), usernameOrEmail),
                 cb.equal(root.get("email"), usernameOrEmail)
             ));

        return getCurrentSession().createQuery(query)
                .getResultStream()
                .findFirst();
    }
}