package com.shop.controller;

import com.shop.dto.ApiResponse;
import com.shop.dto.ProductDto;
import com.shop.entity.Order;
import com.shop.entity.Product;
import com.shop.service.OrderService;
import com.shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getDashboardOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<Order> orders = orderService.findOrdersWithPagination(page, size);
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return ResponseEntity.ok(ApiResponse.success("Product list retrieved successfully", products));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<Product>> createProduct(@Valid @RequestBody ProductDto productDto) {
        Product product = productService.createProduct(productDto);
        return ResponseEntity.ok(ApiResponse.success("Product created successfully", product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto productDto) {

        Product product = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
    }

    @PatchMapping("/orders/{id}/complete")
    public ResponseEntity<ApiResponse<Order>> completeOrder(@PathVariable Long id) {
        Order order = orderService.completeOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order marked as completed", order));
    }

    @PatchMapping("/orders/{id}/cancel")
    public ResponseEntity<ApiResponse<Order>> cancelOrder(@PathVariable Long id) {
        Order order = orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order has been cancelled", order));
    }

    @GetMapping("/statistics/total-sales")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalSales() {
        BigDecimal totalSales = orderService.calculateTotalSales();
        return ResponseEntity.ok(ApiResponse.success("Total sales calculated successfully", totalSales));
    }

    @GetMapping("/statistics/top-selling-products")
    public ResponseEntity<ApiResponse<List<Object[]>>> getTopSellingProducts(
            @RequestParam(defaultValue = "3") int limit) {

        List<Object[]> topProducts = orderService.findTopSellingProducts(limit);
        return ResponseEntity.ok(ApiResponse.success("Top-selling products retrieved successfully", topProducts));
    }

    @GetMapping("/statistics/most-profitable-products")
    public ResponseEntity<ApiResponse<List<Object[]>>> getMostProfitableProducts() {
        List<Object[]> profitableProducts = orderService.findMostProfitableProducts();
        return ResponseEntity.ok(ApiResponse.success("Most profitable products retrieved successfully", profitableProducts));
    }

    @GetMapping("/statistics/total-sold-quantity")
    public ResponseEntity<ApiResponse<Long>> getTotalSoldQuantity() {
        Long totalQuantity = orderService.calculateTotalSoldQuantity();
        return ResponseEntity.ok(ApiResponse.success("Total sold quantity retrieved successfully", totalQuantity));
    }

    @GetMapping("/products/low-stock")
    public ResponseEntity<ApiResponse<List<Product>>> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold) {

        List<Product> lowStockProducts = productService.findLowStockProducts(threshold);
        return ResponseEntity.ok(ApiResponse.success("Low stock products retrieved successfully", lowStockProducts));
    }
}