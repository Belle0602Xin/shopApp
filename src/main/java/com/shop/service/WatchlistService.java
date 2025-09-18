package com.shop.service;

import com.shop.entity.Product;
import com.shop.entity.User;
import com.shop.entity.Watchlist;
import com.shop.exception.BusinessException;
import com.shop.exception.ResourceNotFoundException;
import com.shop.repository.WatchlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    public Watchlist addToWatchlist(Long userId, Long productId) {
        User user = userService.findById(userId);
        Product product = productService.findById(productId);

        if (watchlistRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new BusinessException("Product is already in the watchlist");
        }

        Watchlist watchlist = new Watchlist(user, product);
        return watchlistRepository.save(watchlist);
    }

    public void removeFromWatchlist(Long userId, Long productId) {
        if (!watchlistRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new ResourceNotFoundException("Watchlist item not found");
        }

        watchlistRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public List<Watchlist> findWatchlistByUser(Long userId) {
        return watchlistRepository.findByUserId(userId);
    }

    public List<Watchlist> findAvailableWatchlistByUser(Long userId) {
        return watchlistRepository.findAvailableWatchlistByUserId(userId);
    }

    public List<Product> findWatchedProductsByUser(Long userId) {
        return watchlistRepository.findWatchedProductsByUserId(userId);
    }

    public boolean isProductInWatchlist(Long userId, Long productId) {
        return watchlistRepository.existsByUserIdAndProductId(userId, productId);
    }

    public long countWatchlistByUser(Long userId) {
        return watchlistRepository.countByUserId(userId);
    }

    public Optional<Watchlist> findByUserIdAndProductId(Long userId, Long productId) {
        return watchlistRepository.findByUserIdAndProductId(userId, productId);
    }
}