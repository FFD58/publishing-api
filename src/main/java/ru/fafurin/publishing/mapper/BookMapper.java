package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.BookRequest;
import ru.fafurin.publishing.dto.response.BookResponse;
import ru.fafurin.publishing.entity.Book;

import java.util.Arrays;

public class BookMapper {

    public static Book getBook(Book book, BookRequest bookRequest) {
        book.setTitle(bookRequest.getTitle());

        String[] authorsArray = bookRequest.getAuthors().split(",");
        book.setAuthors(Arrays.stream(authorsArray).toList());

//        if (!bookRequest.getFiles().isEmpty()) {
//            List<String> filePaths = bookRequest.getFiles();
//
//            List<BookFile> files = filePaths.stream().map(f -> BookFileMapper.getBookFile(new BookFile(), f)).toList();
//            book.addFiles(files);
//        }
        return book;
    }

    public static BookResponse getBookResponse(Book book) {

        String authors = String.join(",", book.getAuthors());

        return BookResponse.builder()
                .title(book.getTitle())
                .authors(authors)
                .format(book.getFormat().getTitle())
                .type(book.getType().getTitle())
                .build();
    }

}
