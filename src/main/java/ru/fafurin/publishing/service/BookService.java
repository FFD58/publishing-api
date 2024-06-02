package ru.fafurin.publishing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.BookRequest;
import ru.fafurin.publishing.exception.BookFormatNotFoundException;
import ru.fafurin.publishing.exception.BookNotFoundException;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.exception.OrderNotFoundException;
import ru.fafurin.publishing.mapper.BookMapper;
import ru.fafurin.publishing.model.Book;
import ru.fafurin.publishing.model.BookFormat;
import ru.fafurin.publishing.model.BookType;
import ru.fafurin.publishing.model.Order;
import ru.fafurin.publishing.repository.BookFormatRepository;
import ru.fafurin.publishing.repository.BookRepository;
import ru.fafurin.publishing.repository.BookTypeRepository;
import ru.fafurin.publishing.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookFormatRepository bookFormatRepository;
    private final BookTypeRepository bookTypeRepository;
    private final OrderRepository orderRepository;

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

        Long typeId = bookRequest.getBookTypeId();
        BookType bookType = bookTypeRepository.findById(typeId)
                .orElseThrow(() -> new BookTypeNotFoundException(typeId));
        book.setType(bookType);

        Long formatId = bookRequest.getBookFormatId();
        BookFormat bookFormat = bookFormatRepository.findById(formatId)
                .orElseThrow(() -> new BookFormatNotFoundException(formatId));
        book.setFormat(bookFormat);

        Long orderId = bookRequest.getOrderId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        book.setOrder(order);

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
