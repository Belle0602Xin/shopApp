package com.shop.dto;

import java.math.BigDecimal;

public class SalesStatisticsDto {
    private Long totalOrders;
    private BigDecimal totalSales;
    private Long totalSoldQuantity;
    private Long processingOrders;
    private Long completedOrders;
    private Long canceledOrders;

    public SalesStatisticsDto() {}

    public SalesStatisticsDto(Long totalOrders, BigDecimal totalSales, Long totalSoldQuantity,
                             Long processingOrders, Long completedOrders, Long canceledOrders) {
        this.totalOrders = totalOrders;
        this.totalSales = totalSales;
        this.totalSoldQuantity = totalSoldQuantity;
        this.processingOrders = processingOrders;
        this.completedOrders = completedOrders;
        this.canceledOrders = canceledOrders;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public Long getTotalSoldQuantity() {
        return totalSoldQuantity;
    }

    public void setTotalSoldQuantity(Long totalSoldQuantity) {
        this.totalSoldQuantity = totalSoldQuantity;
    }

    public Long getProcessingOrders() {
        return processingOrders;
    }

    public void setProcessingOrders(Long processingOrders) {
        this.processingOrders = processingOrders;
    }

    public Long getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Long completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Long getCanceledOrders() {
        return canceledOrders;
    }

    public void setCanceledOrders(Long canceledOrders) {
        this.canceledOrders = canceledOrders;
    }
}