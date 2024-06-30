package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.entity.BookFile;

public class BookFileMapper {

    public static BookFile getBookFile(BookFile bookFile, String filePath) {
        bookFile.setPath(filePath);
        return bookFile;
    }

}
