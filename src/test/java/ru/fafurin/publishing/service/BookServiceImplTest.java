package ru.fafurin.publishing.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fafurin.publishing.dto.request.BookRequest;
import ru.fafurin.publishing.exception.BookNotFoundException;
import ru.fafurin.publishing.entity.*;
import ru.fafurin.publishing.repository.BookFormatRepository;
import ru.fafurin.publishing.repository.BookRepository;
import ru.fafurin.publishing.repository.BookTypeRepository;
import ru.fafurin.publishing.repository.OrderRepository;
import ru.fafurin.publishing.service.impl.BookServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl service;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookTypeRepository bookTypeRepository;
    @Mock
    private BookFormatRepository bookFormatRepository;
    @Mock
    private OrderRepository orderRepository;
    private Book book;
    private BookRequest bookRequest;
    private Long bookId;

    @BeforeEach
    public void init() {
        bookId = 111L;
        bookRequest = BookRequest.builder()
                .title("Test Book")
                .typeId(1L)
                .formatId(1L)
                .authors(List.of("Test Author"))
                .files(List.of("test.file"))
                .build();
        book = Book.builder()
                .title("Test Book")
                .type(Mockito.mock(BookType.class))
                .format(Mockito.mock(BookFormat.class))
                .order(Mockito.mock(Order.class))
                .authors(List.of("Test Author"))
                .files(List.of(Mockito.mock(BookFile.class)))
                .build();
    }

    /**
     * Проверить получение всех книг
     */
    @Test
    public void GetAll_ReturnsList() {
        Book book1 = Book.builder()
                .id(2L)
                .title("Another Book Type")
                .isDeleted(false)
                .build();

        when(bookRepository.findAll()).thenReturn(List.of(book, book1));

        List<Book> savedBooks = service.getAll();

        Assertions.assertFalse(savedBooks.isEmpty());

    }

    /**
     * Проверить сохранение книги
     */
    @Test
    public void Save_ReturnsBook() {
        BookType bookType = Mockito.mock(BookType.class);
        when(bookTypeRepository.findById(1L))
                .thenReturn(Optional.ofNullable(bookType));

        BookFormat bookFormat = Mockito.mock(BookFormat.class);
        when(bookFormatRepository.findById(1L))
                .thenReturn(Optional.ofNullable(bookFormat));

        Order order = Mockito.mock(Order.class);
        when(orderRepository.findById(1L))
                .thenReturn(Optional.ofNullable(order));

        when(bookRepository.save(Mockito.any(Book.class)))
                .thenReturn(Mockito.mock(Book.class));

        Assertions.assertNotNull(service.save(bookRequest));
    }

    /**
     * Проверить получение книги по идентификатору
     */
    @Test
    public void FindById_ReturnsBook() {
        when(bookRepository.findById(bookId))
                .thenReturn(Optional.ofNullable(book));

        Assertions.assertNotNull(service.get(bookId));
    }

    /**
     * Проверить исключение BookNotFoundException если книга не найдена по идентификатору
     */
    @Test
    public void FindById_ReturnsBookNotFoundException() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(BookNotFoundException.class, () -> {
            service.get(bookId);
        });
        String expectedMessage = String.format("Book with id: %d not found", bookId);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Проверить изменение книги по идентификатору
     */
    @Test
    public void UpdateById_ReturnsBook() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(book)).thenReturn(book);

        Assertions.assertNotNull(service.update(bookId, bookRequest));
    }

    /**
     * Проверить безопасное удаление книги по идентификатору
     */
    @Test
    public void SafeDeleteById_ReturnsVoid() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(book)).thenReturn(book);

        service.delete(bookId);

        Assertions.assertTrue(book.isDeleted());
    }

}
