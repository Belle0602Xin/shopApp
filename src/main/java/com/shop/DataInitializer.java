package com.shop;

import com.shop.dto.UserRegistrationDto;
import com.shop.entity.User;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


//@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;

    // @Autowired
    // private ProductService productService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            initializeData();
            System.out.println("Data initialization completed!");
        } catch (Exception e) {
            System.out.println("Error occurred during data initialization, data may already exist: " + e.getMessage());
        }
    }

    private void initializeData() {
        try {
            UserRegistrationDto adminDto = new UserRegistrationDto("admin", "admin@shop.com", "admin123");
            userService.registerUser(adminDto);
            System.out.println("Admin user created successfully");
        } catch (Exception e) {
            System.out.println("Admin user already exists");
        }

        try {
            UserRegistrationDto userDto = new UserRegistrationDto("testuser", "user@shop.com", "user123");
            userService.registerUser(userDto);
            System.out.println("Test user created successfully");
        } catch (Exception e) {
            System.out.println("Test user already exists");
        }

        try {
            User admin = userService.findByUsername("admin");
            admin.setRole(User.Role.ADMIN);
            userService.updateUser(admin);
            System.out.println("Admin role set successfully");
        } catch (Exception e) {
            System.out.println("Failed to set admin role: " + e.getMessage());
        }

        // long productCount = productService.countAvailableProducts();
        // if (productCount == 0) {
        //     createSampleProducts();
        //     System.out.println("Sample products created successfully");
        // } else {
        //     System.out.println("Products already exist, skipping creation");
        // }
    }

    // private void createSampleProducts() {
    //     ProductDto[] products = {
    //         new ProductDto("Apple iPhone 15", new BigDecimal("4500.00"), new BigDecimal("5999.00"), 50),
    //         new ProductDto("Samsung Galaxy S24", new BigDecimal("3800.00"), new BigDecimal("4999.00"), 30),
    //         new ProductDto("Huawei MateBook X Laptop", new BigDecimal("5500.00"), new BigDecimal("6999.00"), 20),
    //         new ProductDto("Xiaomi TV 55-inch", new BigDecimal("2200.00"), new BigDecimal("2999.00"), 15),
    //         new ProductDto("Dell 27-inch Monitor", new BigDecimal("1200.00"), new BigDecimal("1599.00"), 25)
    //     };
    //
    //     for (ProductDto productDto : products) {
    //         try {
    //             productService.createProduct(productDto);
    //         } catch (Exception e) {
    //             System.out.println("Failed to create product: " + e.getMessage());
    //         }
    //     }
    // }
}