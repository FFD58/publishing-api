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
import ru.fafurin.publishing.dto.request.BookTypeRequest;
import ru.fafurin.publishing.exception.BookTypeNotFoundException;
import ru.fafurin.publishing.model.BookType;
import ru.fafurin.publishing.service.BookTypeService;

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
public class BookTypeControllerTest {

    private static final String END_POINT_PATH = "/api/v1/books/types";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookTypeService service;
    private BookType bookType;
    private BookTypeRequest bookTypeRequest;
    private String requestURI;
    private Long bookTypeId;

    @BeforeEach
    public void init() {
        bookType = BookType.builder().id(1L).title("New Book Type").isDeleted(false).build();
        bookTypeRequest = BookTypeRequest.builder().title("New Book Type").build();
        bookTypeId = 111L;
        requestURI = END_POINT_PATH + "/" + bookTypeId;
    }

    /**
     * Проверить статус Created при успешном сохранении нового типа книги
     */
    @Test
    public void Save_Returns201Created() throws Exception {
        when(service.save(bookTypeRequest)).thenReturn(bookType);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookTypeRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(bookType)))
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest в случае, если запрос не прошел валидацию
     */
    @Test
    public void SaveThenInvalidRequest_Returns400BadRequest() throws Exception {
        BookTypeRequest invalidBookTypeRequest = BookTypeRequest.builder()
                .title(null).build();

        String requestBody = objectMapper.writeValueAsString(invalidBookTypeRequest);
        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить статус NotFound в случае, если тип книги не найден по идентификатору
     */
    @Test
    public void GetByIdThenWrongId_Returns404NotFound() throws Exception {
        when(service.get(bookTypeId)).thenThrow(BookTypeNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное получение типа книги по идентификатору
     */
    @Test
    public void GetById_Returns200OK() throws Exception {
        when(service.get(bookTypeId)).thenReturn(bookType);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(bookType)))
                .andDo(print());
    }

    /**
     * Проверить статус NoContent в случае, если вернется пустой список типов книг
     */
    @Test
    public void GetAllThenEmptyList_Returns204NoContent() throws Exception {
        when(service.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Проверить успешное получение списка всех типов книг
     */
    @Test
    public void GetAll_Returns200OK() throws Exception {
        List<BookType> bookTypes = List.of(
                Mockito.mock(BookType.class),
                Mockito.mock(BookType.class),
                Mockito.mock(BookType.class)
        );

        when(service.getAll()).thenReturn(bookTypes);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(bookTypes)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при изменении типа книги, когда тип не найден по идентификатору
     */
    @Test
    public void UpdateThenWrongId_Returns404NotFound() throws Exception {
        when(service.update(bookTypeId, bookTypeRequest)).thenThrow(BookTypeNotFoundException.class);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookTypeRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest при изменении типа книги, если запрос не прошел валидацию
     */
    @Test
    public void UpdateThenInvalidRequest_Returns400BadRequest() throws Exception {
        BookTypeRequest invalidBookTypeRequest = BookTypeRequest.builder()
                .title(null).build();
        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidBookTypeRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить успешное изменение типа книги
     */
    @Test
    public void Update_Returns200OK() throws Exception {
        when(service.update(bookTypeId, bookTypeRequest)).thenReturn(bookType);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookTypeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookType)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при удалении типа книги, когда тип не найден по идентификатору
     */
    @Test
    public void DeleteThenWrongId_Returns404NotFound() throws Exception {
        Mockito.doThrow(BookTypeNotFoundException.class).when(service).delete(bookTypeId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное удаление типа книги
     */
    @Test
    public void Delete_Returns200OK() throws Exception {
        Mockito.doNothing().when(service).delete(bookTypeId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Book type with id: %d deleted", bookTypeId)))
                .andDo(print());
    }

}
