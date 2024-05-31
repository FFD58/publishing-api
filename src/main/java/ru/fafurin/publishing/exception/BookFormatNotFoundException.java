package ru.fafurin.publishing.exception;

public class BookFormatNotFoundException extends AppException {
    public BookFormatNotFoundException(Long id) {
        super(String.format("Book format with id: %d not found", id));
    }
    public BookFormatNotFoundException(String title) {
        super(String.format("Book format with title: %s not found", title));
    }
}
