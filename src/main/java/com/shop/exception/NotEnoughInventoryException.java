package com.shop.exception;

public class NotEnoughInventoryException extends RuntimeException {
    public NotEnoughInventoryException(String message) {
        super(message);
    }

    public NotEnoughInventoryException(Long productId, int requestedQuantity, int availableQuantity) {
        super(String.format("Not enough inventory for product %d. Requested: %d, Available: %d",
                productId, requestedQuantity, availableQuantity));
    }
}