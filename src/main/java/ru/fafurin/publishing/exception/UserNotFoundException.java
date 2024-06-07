package ru.fafurin.publishing.exception;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(Long id) {
        super(String.format("User with id: %d not found", id));
    }
    public UserNotFoundException(String username) {
        super(String.format("User with username: %s not found", username));
    }
}
