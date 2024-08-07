package ru.fafurin.publishing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.request.BookRequest;
import ru.fafurin.publishing.entity.Book;
import ru.fafurin.publishing.entity.BookFormat;
import ru.fafurin.publishing.entity.BookType;
import ru.fafurin.publishing.exception.BookFormatNotFoundException;
import ru.fafurin.publishing.exception.BookNotFoundException;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.mapper.BookMapper;
import ru.fafurin.publishing.repository.BookFormatRepository;
import ru.fafurin.publishing.repository.BookRepository;
import ru.fafurin.publishing.repository.BookTypeRepository;
import ru.fafurin.publishing.repository.OrderRepository;
import ru.fafurin.publishing.service.BookService;
import ru.fafurin.publishing.service.FileService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookFormatRepository bookFormatRepository;
    private final BookTypeRepository bookTypeRepository;

    /**
     * Получить список всех неудаленных книг
     *
     * @return список книг с полем isDeleted = false
     */
    public List<Book> getAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Получить книгу по идентификатору
     *
     * @param id - идентификатор книги
     * @return книга или выбрасывается исключение,
     * если книга не найдена по идентификатору
     */
    public Book get(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    /**
     * Сохранить книгу
     *
     * @param bookRequest - запрос с данными для сохранения новой книги
     * @return сохраненная книга
     */
    public Book save(BookRequest bookRequest) {
        Book book = new Book();

        Long typeId = bookRequest.getTypeId();
        BookType bookType = bookTypeRepository.findById(typeId)
                .orElseThrow(() -> new BookTypeNotFoundException(typeId));
        book.setType(bookType);

        Long formatId = bookRequest.getFormatId();
        BookFormat bookFormat = bookFormatRepository.findById(formatId)
                .orElseThrow(() -> new BookFormatNotFoundException(formatId));
        book.setFormat(bookFormat);

        return bookRepository.save(BookMapper.getBook(book, bookRequest));
    }

    /**
     * Изменить данные книги
     *
     * @param id          - идентификатор книги
     * @param bookRequest - запрос с данными для изменения существующей книги
     * @return измененная книга
     */
    public Book update(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return bookRepository.save(
                BookMapper.getBook(book, bookRequest));
    }

    /**
     * Безопасно удалить тип книги по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     *
     * @param id - идентификатор типа книги
     */
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        book.setDeleted(true);
        bookRepository.save(book);
    }

}
