package ru.fafurin.publishing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.model.Book;
import ru.fafurin.publishing.model.BookFormat;
import ru.fafurin.publishing.model.BookType;
import ru.fafurin.publishing.dto.BookRequest;
import ru.fafurin.publishing.exception.BookFormatNotFoundException;
import ru.fafurin.publishing.exception.BookNotFoundException;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.mapper.BookMapper;
import ru.fafurin.publishing.repository.BookFormatRepository;
import ru.fafurin.publishing.repository.BookRepository;
import ru.fafurin.publishing.repository.BookTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookFormatRepository bookFormatRepository;
    private final BookTypeRepository bookTypeRepository;

    public List<Book> getAll() {

        return bookRepository.findAll();
    }

    public Book save(BookRequest bookRequest) {

        Book book = new Book();

        Long typeId = bookRequest.getBookTypeId();

        if (typeId != null) {
            BookType bookType = bookTypeRepository.findById(typeId)
                    .orElseThrow(() -> new BookTypeNotFoundException(typeId));
            book.setType(bookType);
        }

        Long formatId = bookRequest.getBookFormatId();
        if (formatId != null) {
            BookFormat bookFormat = bookFormatRepository.findById(formatId)
                    .orElseThrow(() -> new BookFormatNotFoundException(formatId));
            book.setFormat(bookFormat);
        }
        return bookRepository.save(BookMapper.getBook(book, bookRequest));
    }

    public Book update(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return bookRepository.save(
                BookMapper.getBook(book, bookRequest));
    }

    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }

}
