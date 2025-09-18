package com.shop.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateOrderDto {

    @NotNull(message = "Order items cannot be null")
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemDto> orderItems;

    public CreateOrderDto() {}

    public CreateOrderDto(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }
}