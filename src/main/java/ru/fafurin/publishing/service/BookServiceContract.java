package ru.fafurin.publishing.service;

import ru.fafurin.publishing.dto.request.BookRequest;
import ru.fafurin.publishing.entity.Book;

public interface BookServiceContract extends CrudServiceContract<Book, BookRequest> {
}
