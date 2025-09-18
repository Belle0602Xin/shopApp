package com.shop.service;

import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.exception.BusinessException;
import com.shop.exception.ResourceNotFoundException;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Transactional
    public Order createOrder(Long userId, List<OrderItemDto> orderItems) {
        User user = userService.findById(userId);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PROCESSING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemDto itemDto : orderItems) {
            Product product = productService.findById(itemDto.getProductId());

            if (product.getInventory() < itemDto.getQuantity()) {
                throw new com.shop.exception.NotEnoughInventoryException(
                        product.getId(), itemDto.getQuantity(), product.getInventory());
            }

            productService.reduceInventory(product.getId(), itemDto.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.getRetailPrice());
            orderItem.setSubtotal(product.getRetailPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            order.addOrderItem(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }

        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = findById(orderId);

        if (!order.canBeCompleted()) {
            throw new BusinessException("Order status does not allow completion");
        }

        order.setStatus(Order.OrderStatus.COMPLETED);
        return orderRepository.update(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = findById(orderId);

        if (!order.canBeCanceled()) {
            throw new BusinessException("Order completed status does not allow cancellation");
        }

        for (OrderItem item : order.getOrderItems()) {
            productService.restoreInventory(item.getProduct().getId(), item.getQuantity());
        }

        order.setStatus(Order.OrderStatus.CANCELED);
        return orderRepository.update(order);
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> findOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Order> findOrdersWithPagination(int page, int size) {
        return orderRepository.findOrdersWithPagination(page, size);
    }

    @Transactional(readOnly = true)
    public List<Order> findRecentCompletedOrdersByUser(Long userId, int limit) {
        return orderRepository.findRecentCompletedOrdersByUser(userId, limit);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findRecentPurchasesByUser(Long userId, int limit) {
        return orderItemRepository.findRecentPurchasesByUser(userId, limit);
    }

    @Transactional(readOnly = true)
    public List<Object[]> findTopSellingProducts(int limit) {
        return orderItemRepository.findTopSellingProducts(limit);
    }

    public List<Object[]> findMostProfitableProducts() {
        return orderItemRepository.findMostProfitableProducts();
    }

    public BigDecimal calculateTotalSales() {
        return orderItemRepository.calculateTotalSales();
    }

    public Long calculateTotalSoldQuantity() {
        return orderItemRepository.calculateTotalSoldQuantity();
    }

    public long countOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
}