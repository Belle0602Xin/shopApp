package com.shop.controller;

import com.shop.dto.ApiResponse;
import com.shop.dto.OrderItemDto;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.service.OrderService;
import com.shop.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody List<OrderItemDto> orderItems) {

        String jwt = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        Order order = orderService.createOrder(userId, orderItems);
        return ResponseEntity.ok(ApiResponse.success("Order created successfully", order));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        Order order = orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", order));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<Order>>> getMyOrders(
            @RequestHeader("Authorization") String token) {

        String jwt = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        List<Order> orders = orderService.findOrdersByUser(userId);
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Order details retrieved successfully", order));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<ApiResponse<List<OrderItem>>> getOrderItems(@PathVariable Long id) {
        List<OrderItem> orderItems = orderService.findOrderItemsByOrderId(id);
        return ResponseEntity.ok(ApiResponse.success("Order items retrieved successfully", orderItems));
    }

    @GetMapping("/recent-purchases")
    public ResponseEntity<ApiResponse<List<OrderItem>>> getRecentPurchases(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "5") int limit) {

        String jwt = token.replace("Bearer ", "");
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        List<OrderItem> recentPurchases = orderService.findRecentPurchasesByUser(userId, limit);
        return ResponseEntity.ok(ApiResponse.success("Recent purchases retrieved successfully", recentPurchases));
    }
}