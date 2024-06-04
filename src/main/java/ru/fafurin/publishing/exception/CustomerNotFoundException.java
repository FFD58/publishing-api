package ru.fafurin.publishing.exception;

public class CustomerNotFoundException extends AppException {
    public CustomerNotFoundException(Long id) {
        super(String.format("Customer with id: %d not found", id));
    }

    public CustomerNotFoundException(String email) {
        super(String.format("Customer with email: %s not found", email));
    }
}
