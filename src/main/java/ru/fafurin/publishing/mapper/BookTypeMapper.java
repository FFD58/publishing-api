package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.BookTypeRequest;
import ru.fafurin.publishing.model.BookType;

public class BookTypeMapper {

    public static BookType getBookType(BookType bookType, BookTypeRequest bookTypeRequest) {
        bookType.setTitle(bookTypeRequest.getTitle());
        return bookType;
    }

}
