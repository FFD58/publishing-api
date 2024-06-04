package ru.fafurin.publishing.exception;

public class UserTaskNotFoundException extends AppException {
    public UserTaskNotFoundException(Long id) {
        super(String.format("User task with id: %d not found", id));
    }
}
