package ru.fafurin.publishing.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fafurin.publishing.dto.request.BookFormatRequest;
import ru.fafurin.publishing.exception.BookFormatNotFoundException;
import ru.fafurin.publishing.entity.BookFormat;
import ru.fafurin.publishing.repository.BookFormatRepository;
import ru.fafurin.publishing.service.impl.BookFormatServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookFormatServiceTest {

    @InjectMocks
    private BookFormatServiceImpl service;

    @Mock
    private BookFormatRepository repository;
    private BookFormat bookFormat;
    private BookFormatRequest bookFormatRequest;
    private Long bookFormatId;

    @BeforeEach
    public void init() {
        bookFormatId = 111L;
        bookFormat = BookFormat.builder()
                .id(111L)
                .title("Test Book Format")
                .designation("Test Designation")
                .isDeleted(false)
                .build();
        bookFormatRequest = BookFormatRequest.builder()
                .title("Test Book Format")
                .designation("Test Designation")
                .build();
    }

    /**
     * Проверить получение всех форматов книг
     */
    @Test
    public void GetAll_ReturnsList() {
        BookFormat bookFormat1 = BookFormat.builder()
                .id(2L)
                .title("Another Book Format")
                .designation("Test Designation")
                .isDeleted(false)
                .build();

        when(repository.findAll()).thenReturn(List.of(bookFormat, bookFormat1));

        List<BookFormat> savedBookFormats = service.getAll();

        Assertions.assertFalse(savedBookFormats.isEmpty());

    }

    /**
     * Проверить сохранение формата книги
     */
    @Test
    public void Save_ReturnsBookFormat() {
        when(repository.save(Mockito.any(BookFormat.class))).thenReturn(bookFormat);

        Assertions.assertNotNull(service.save(bookFormatRequest));
    }

    /**
     * Проверить получение формата книги по идентификатору
     */
    @Test
    public void FindById_ReturnsBookFormat() {
        when(repository.findById(bookFormatId)).thenReturn(Optional.ofNullable(bookFormat));

        Assertions.assertNotNull(service.get(bookFormatId));
    }

    /**
     * Проверить исключение BookFormatNotFoundException если формат книги не найден по идентификатору
     */
    @Test
    public void FindById_ReturnsBookFormatNotFoundException() {
        when(repository.findById(bookFormatId)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(BookFormatNotFoundException.class, () -> {
            service.get(bookFormatId);
        });
        String expectedMessage = String.format("Book format with id: %d not found", bookFormatId);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Проверить изменение формата книги по идентификатору
     */
    @Test
    public void UpdateById_ReturnsBookFormat() {
        when(repository.findById(bookFormatId)).thenReturn(Optional.ofNullable(bookFormat));
        when(repository.save(bookFormat)).thenReturn(bookFormat);

        Assertions.assertNotNull(service.update(bookFormatId, bookFormatRequest));
    }

    /**
     * Проверить безопасное удаление формата книги по идентификатору
     */
    @Test
    public void SafeDeleteById_ReturnsVoid() {
        when(repository.findById(bookFormatId)).thenReturn(Optional.ofNullable(bookFormat));
        when(repository.save(bookFormat)).thenReturn(bookFormat);

        service.delete(bookFormatId);

        Assertions.assertTrue(bookFormat.isDeleted());
    }

}
