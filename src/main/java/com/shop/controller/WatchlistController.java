package com.shop.controller;

import com.shop.dto.ApiResponse;
import com.shop.entity.Product;
import com.shop.entity.Watchlist;
import com.shop.security.JwtTokenProvider;
import com.shop.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/add/{productId}")
    public ResponseEntity<ApiResponse<Watchlist>> addToWatchlist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId) {

        String jwt = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        Watchlist watchlist = watchlistService.addToWatchlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Product added to watchlist successfully", watchlist));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<String>> removeFromWatchlist(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId) {

        String jwt = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        watchlistService.removeFromWatchlist(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Product removed from watchlist successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Watchlist>>> getWatchlist(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        List<Watchlist> watchlist = watchlistService.findWatchlistByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Watchlist retrieved successfully", watchlist));
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Watchlist>>> getAvailableWatchlist(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        List<Watchlist> watchlist = watchlistService.findAvailableWatchlistByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Available products in watchlist retrieved successfully", watchlist));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getWatchedProducts(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        List<Product> products = watchlistService.findWatchedProductsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Watched products retrieved successfully", products));
    }
}