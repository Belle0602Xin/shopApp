package com.shop.controller;

import com.shop.dto.ApiResponse;
import com.shop.dto.ProductUserDto;
import com.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductUserDto>>> getAllAvailableProducts() {
        List<ProductUserDto> products = productService.findAvailableProductsForUser();
        return ResponseEntity.ok(ApiResponse.success("Product list retrieved successfully", products));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductUserDto>>> searchProducts(
            @RequestParam String keyword) {
        List<ProductUserDto> products = productService.searchProductsForUser(keyword);
        return ResponseEntity.ok(ApiResponse.success("Products searched successfully", products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductUserDto>> getProduct(@PathVariable Long id) {
        ProductUserDto product = productService.findProductForUser(id);
        return ResponseEntity.ok(ApiResponse.success("Product details retrieved successfully", product));
    }
}