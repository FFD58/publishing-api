package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.BookFormatRequest;
import ru.fafurin.publishing.model.BookFormat;

public class BookFormatMapper {
    public static BookFormat getBookFormat(BookFormat bookFormat, BookFormatRequest bookFormatRequest) {
        bookFormat.setTitle(bookFormatRequest.getTitle());
        bookFormat.setDesignation(bookFormatRequest.getDesignation());
        return bookFormat;
    }

}
