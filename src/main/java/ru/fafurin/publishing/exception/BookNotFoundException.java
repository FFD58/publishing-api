package ru.fafurin.publishing.exception;

public class BookNotFoundException extends AppException {
    public BookNotFoundException(Long id) {
        super(String.format("Book with id: %s not found", id));
    }
}
