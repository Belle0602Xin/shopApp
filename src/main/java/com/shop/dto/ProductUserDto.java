package com.shop.dto;

import java.math.BigDecimal;

public class ProductUserDto {

    private Long id;
    private String description;
    private BigDecimal retailPrice;
    private boolean inStock;

    public ProductUserDto() {}

    public ProductUserDto(Long id, String description, BigDecimal retailPrice, boolean inStock) {
        this.id = id;
        this.description = description;
        this.retailPrice = retailPrice;
        this.inStock = inStock;
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

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}