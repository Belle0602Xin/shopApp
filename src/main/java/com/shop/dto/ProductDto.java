package com.shop.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductDto {

    private Long id;

    @NotBlank(message = "Product description cannot be blank")
    private String description;

    @NotNull(message = "Wholesale price cannot be null")
    @DecimalMin(value = "0.01", message = "Wholesale price must be greater than 0")
    private BigDecimal wholesalePrice;

    @NotNull(message = "Retail price cannot be null")
    @DecimalMin(value = "0.01", message = "Retail price must be greater than 0")
    private BigDecimal retailPrice;

    @NotNull(message = "Inventory quantity cannot be null")
    @Min(value = 0, message = "Inventory quantity cannot be negative")
    private Integer inventory;

    public ProductDto() {}

    public ProductDto(String description, BigDecimal wholesalePrice, BigDecimal retailPrice, Integer inventory) {
        this.description = description;
        this.wholesalePrice = wholesalePrice;
        this.retailPrice = retailPrice;
        this.inventory = inventory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(BigDecimal wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }
}