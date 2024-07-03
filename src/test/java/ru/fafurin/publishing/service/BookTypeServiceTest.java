package ru.fafurin.publishing.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fafurin.publishing.dto.request.BookTypeRequest;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.entity.BookType;
import ru.fafurin.publishing.repository.BookTypeRepository;
import ru.fafurin.publishing.service.impl.BookTypeServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookTypeServiceTest {

    @InjectMocks
    private BookTypeServiceImpl service;

    @Mock
    private BookTypeRepository repository;
    private BookType bookType;
    private BookTypeRequest bookTypeRequest;
    private Long bookTypeId;

    @BeforeEach
    public void init() {
        bookTypeId = 111L;
        bookType = BookType.builder()
                .id(111L)
                .title("Test Book Type")
                .isDeleted(false)
                .build();
        bookTypeRequest = BookTypeRequest.builder()
                .title("Test Book Type")
                .build();
    }

    /**
     * Проверить получение всех типов книг
     */
    @Test
    public void GetAll_ReturnsList() {
        BookType bookType1 = BookType.builder()
                .id(2L)
                .title("Another Book Type")
                .isDeleted(false)
                .build();

        when(repository.findAll()).thenReturn(List.of(bookType, bookType1));

        List<BookType> savedBookTypes = service.getAll();

        Assertions.assertFalse(savedBookTypes.isEmpty());

    }

    /**
     * Проверить сохранение типа книги
     */
    @Test
    public void Save_ReturnsBookType() {
        when(repository.save(Mockito.any(BookType.class))).thenReturn(bookType);

        Assertions.assertNotNull(service.save(bookTypeRequest));
    }

    /**
     * Проверить получение типа книги по идентификатору
     */
    @Test
    public void FindById_ReturnsBookType() {
        when(repository.findById(bookTypeId)).thenReturn(Optional.ofNullable(bookType));

        Assertions.assertNotNull(service.get(bookTypeId));
    }

    /**
     * Проверить исключение BookTypeNotFoundException если тип книги не найден по идентификатору
     */
    @Test
    public void FindById_ReturnsBookTypeNotFoundException() {
        when(repository.findById(bookTypeId)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(BookTypeNotFoundException.class, () -> {
            service.get(bookTypeId);
        });
        String expectedMessage = String.format("Book type with id: %d not found", bookTypeId);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Проверить изменение типа книги по идентификатору
     */
    @Test
    public void UpdateById_ReturnsBookType() {
        when(repository.findById(bookTypeId)).thenReturn(Optional.ofNullable(bookType));
        when(repository.save(bookType)).thenReturn(bookType);

        Assertions.assertNotNull(service.update(bookTypeId, bookTypeRequest));
    }

    /**
     * Проверить безопасное удаление типа книги по идентификатору
     */
    @Test
    public void SafeDeleteById_ReturnsVoid() {
        when(repository.findById(bookTypeId)).thenReturn(Optional.ofNullable(bookType));
        when(repository.save(bookType)).thenReturn(bookType);

        service.delete(bookTypeId);

        Assertions.assertTrue(bookType.isDeleted());
    }

}
