package ru.fafurin.publishing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.fafurin.publishing.dto.request.BookRequest;
import ru.fafurin.publishing.exception.BookNotFoundException;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.entity.*;
import ru.fafurin.publishing.service.impl.BookServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private static final String END_POINT_PATH = "/api/v1/books";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookServiceImpl service;
    private Book book;
    private BookRequest bookRequest;
    private String requestURI;
    private Long bookId;

    @BeforeEach
    public void init() {
        bookRequest = BookRequest.builder()
                .title("Test Book")
                .typeId(1L)
                .formatId(1L)
                .authors("Test Author")
                .build();
        book = Book.builder()
                .title("Test Book")
                .type(Mockito.mock(BookType.class))
                .format(Mockito.mock(BookFormat.class))
                .order(Mockito.mock(Order.class))
                .authors(List.of("Test Author"))
                .files(List.of(Mockito.mock(BookFile.class)))
                .build();
        bookId = 111L;
        requestURI = END_POINT_PATH + "/" + bookId;
    }

    /**
     * Проверить статус Created при успешном сохранении новой книги
     */
    @Test
    public void Save_Returns201Created() throws Exception {
        when(service.save(bookRequest)).thenReturn(book);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(book)))
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest в случае, если запрос не прошел валидацию
     */
    @Test
    public void SaveThenInvalidRequest_Returns400BadRequest() throws Exception {
        BookRequest invalidBookRequest = BookRequest.builder()
                .title(null).build();

        String requestBody = objectMapper.writeValueAsString(invalidBookRequest);
        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить статус NotFound в случае, если книга не найдена по идентификатору
     */
    @Test
    public void GetByIdThenWrongId_Returns404NotFound() throws Exception {
        when(service.get(bookId)).thenThrow(BookTypeNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное получение книги по идентификатору
     */
    @Test
    public void GetById_Returns200OK() throws Exception {
        when(service.get(bookId)).thenReturn(book);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(book)))
                .andDo(print());
    }

    /**
     * Проверить статус NoContent в случае, если вернется пустой список книг
     */
    @Test
    public void GetAllThenEmptyList_Returns204NoContent() throws Exception {
        when(service.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Проверить успешное получение списка всех книг
     */
    @Test
    public void GetAll_Returns200OK() throws Exception {
        List<Book> books = List.of(
                Mockito.mock(Book.class),
                Mockito.mock(Book.class),
                Mockito.mock(Book.class)
        );

        when(service.getAll()).thenReturn(books);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(books)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при изменении книги, когда книга не найдена по идентификатору
     */
    @Test
    public void UpdateThenWrongId_Returns404NotFound() throws Exception {
        when(service.update(bookId, bookRequest)).thenThrow(BookNotFoundException.class);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest при изменении данных книги, если запрос не прошел валидацию
     */
    @Test
    public void UpdateThenInvalidRequest_Returns400BadRequest() throws Exception {
        BookRequest invalidBookRequest = BookRequest.builder()
                .title(null).build();

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidBookRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить успешное изменение книги
     */
    @Test
    public void Update_Returns200OK() throws Exception {
        when(service.update(bookId, bookRequest)).thenReturn(book);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при удалении книги, когда книга не найдена по идентификатору
     */
    @Test
    public void DeleteThenWrongId_Returns404NotFound() throws Exception {
        Mockito.doThrow(BookTypeNotFoundException.class).when(service).delete(bookId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное удаление книги
     */
    @Test
    public void Delete_Returns200OK() throws Exception {
        Mockito.doNothing().when(service).delete(bookId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Book with id: %d deleted", bookId)))
                .andDo(print());
    }
}
