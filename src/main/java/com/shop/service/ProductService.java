package com.shop.service;

import com.shop.dto.ProductDto;
import com.shop.dto.ProductUserDto;
import com.shop.entity.Product;
import com.shop.exception.ResourceNotFoundException;
import com.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Transactional
    public Product createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setDescription(productDto.getDescription());
        product.setWholesalePrice(productDto.getWholesalePrice());
        product.setRetailPrice(productDto.getRetailPrice());
        product.setInventory(productDto.getInventory());

        return productRepository.save(product);
    }
    @Transactional
    public Product updateProduct(Long id, ProductDto productDto) {
        Product product = findById(id);
        product.setDescription(productDto.getDescription());
        product.setWholesalePrice(productDto.getWholesalePrice());
        product.setRetailPrice(productDto.getRetailPrice());
        product.setInventory(productDto.getInventory());

        return productRepository.update(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ProductUserDto> findAvailableProductsForUser() {
        List<Product> products = productRepository.findAvailableProducts();
        return products.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductUserDto> searchProductsForUser(String keyword) {
        List<Product> products = productRepository.findAvailableProductsByDescriptionContaining(keyword);
        return products.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductUserDto findProductForUser(Long id) {
        Product product = findById(id);
        return convertToUserDto(product);
    }
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }
    @Transactional(readOnly = true)
    public synchronized void reduceInventory(Long productId, int quantity) {
        Product product = findById(productId);
        if (product.getInventory() < quantity) {
            throw new com.shop.exception.NotEnoughInventoryException(
                productId, quantity, product.getInventory());
        }
        product.setInventory(product.getInventory() - quantity);
        productRepository.update(product);
    }
    @Transactional(readOnly = true)
    public synchronized void restoreInventory(Long productId, int quantity) {
        Product product = findById(productId);
        product.setInventory(product.getInventory() + quantity);
        productRepository.update(product);
    }

    @Transactional(readOnly = true)
    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    @Transactional(readOnly = true)
    public long countAvailableProducts() {
        return productRepository.countAvailableProducts();
    }
    @Transactional(readOnly = true)
    private ProductUserDto convertToUserDto(Product product) {
        return new ProductUserDto(
            product.getId(),
            product.getDescription(),
            product.getRetailPrice(),
            product.getInventory() > 0
        );
    }
    @Transactional(readOnly = true)
    public ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setDescription(product.getDescription());
        dto.setWholesalePrice(product.getWholesalePrice());
        dto.setRetailPrice(product.getRetailPrice());
        dto.setInventory(product.getInventory());
        return dto;
    }
}
