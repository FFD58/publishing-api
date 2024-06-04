package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.model.Book;
import ru.fafurin.publishing.model.BookFile;
import ru.fafurin.publishing.dto.BookRequest;

import java.util.List;

public class BookMapper {

    public static Book getBook(Book book, BookRequest bookRequest) {
        book.setTitle(bookRequest.getTitle());
        book.setAuthors(bookRequest.getAuthors());

        if (bookRequest.getFiles().size() != 0) {
            List<String> filePaths = bookRequest.getFiles();

            List<BookFile> files = filePaths.stream().map(f -> BookFileMapper.getBookFile(new BookFile(), f)).toList();
            book.addFiles(files);
        }
        return book;
    }
}
