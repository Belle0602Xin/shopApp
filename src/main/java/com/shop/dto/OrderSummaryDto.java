package com.shop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummaryDto {
    private Long orderId;
    private LocalDateTime orderTime;
    private String status;
    private BigDecimal totalAmount;
    private String buyerUsername;

    public OrderSummaryDto() {}

    public OrderSummaryDto(Long orderId, LocalDateTime orderTime, String status,
                          BigDecimal totalAmount, String buyerUsername) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.status = status;
        this.totalAmount = totalAmount;
        this.buyerUsername = buyerUsername;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public void setBuyerUsername(String buyerUsername) {
        this.buyerUsername = buyerUsername;
    }
}