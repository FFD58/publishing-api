package ru.fafurin.publishing.exception;

public class OrderNotFoundException extends AppException {
    public OrderNotFoundException(Long orderId) {
        super(String.format("Order with id: %d not found", orderId));
    }
}
