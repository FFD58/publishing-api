package ru.fafurin.publishing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.fafurin.publishing.dto.BookFormatRequest;
import ru.fafurin.publishing.exception.BookFormatNotFoundException;
import ru.fafurin.publishing.model.BookFormat;
import ru.fafurin.publishing.service.BookFormatService;

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
public class BookFormatControllerTest {

    private static final String END_POINT_PATH = "/api/v1/books/formats";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookFormatService service;
    private BookFormat bookFormat;
    private BookFormatRequest bookFormatRequest;
    private String requestURI;
    private Long bookFormatId;

    @BeforeEach
    public void init() {
        bookFormat = BookFormat.builder()
                .id(1L)
                .title("New Book Type")
                .designation("Test Designation")
                .isDeleted(false).build();
        bookFormatRequest = BookFormatRequest.builder()
                .title("New Book Type")
                .designation("Test Designation").build();
        bookFormatId = 111L;
        requestURI = END_POINT_PATH + "/" + bookFormatId;
    }

    /**
     * Проверить статус Created при успешном сохранении нового формата книги
     */
    @Test
    public void Save_Returns201Created() throws Exception {
        when(service.save(bookFormatRequest)).thenReturn(bookFormat);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookFormatRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(bookFormat)))
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest в случае, если запрос не прошел валидацию
     */
    @Test
    public void SaveThenInvalidRequest_Returns400BadRequest() throws Exception {
        BookFormatRequest invalidBookFormatRequest = BookFormatRequest.builder()
                .title(null)
                .designation("Test Designation")
                .build();

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidBookFormatRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить статус NotFound в случае, если формат книги не найден по идентификатору
     */
    @Test
    public void GetByIdThenWrongId_Returns404NotFound() throws Exception {
        when(service.get(bookFormatId)).thenThrow(BookFormatNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное получение формата книги по идентификатору
     */
    @Test
    public void GetById_Returns200OK() throws Exception {
        BookFormat bookFormat = Mockito.mock(BookFormat.class);

        when(service.get(bookFormatId)).thenReturn(bookFormat);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(bookFormat)))
                .andDo(print());
    }

    /**
     * Проверить статус NoContent в случае, если вернется пустой список форматов книг
     */
    @Test
    public void GetAllThenEmptyList_Returns204NoContent() throws Exception {
        when(service.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Проверить успешное получение списка всех форматов книг
     */
    @Test
    public void GetAll_Returns200OK() throws Exception {
        List<BookFormat> bookFormats = List.of(
                Mockito.mock(BookFormat.class),
                Mockito.mock(BookFormat.class),
                Mockito.mock(BookFormat.class)
        );

        when(service.getAll()).thenReturn(bookFormats);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(bookFormats)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при изменении формата книги, когда формат не найден по идентификатору
     */
    @Test
    public void UpdateThenWrongId_Returns404NotFound() throws Exception {
        when(service.update(bookFormatId, bookFormatRequest))
                .thenThrow(BookFormatNotFoundException.class);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookFormatRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest при изменении формата книги, если запрос не прошел валидацию
     */
    @Test
    public void UpdateThenInvalidRequest_Returns400BadRequest() throws Exception {
        BookFormatRequest invalidBookFormatRequest = BookFormatRequest.builder()
                .title(null)
                .designation("Test Designation")
                .build();

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidBookFormatRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить успешное изменение формата книги
     */
    @Test
    public void Update_Returns200OK() throws Exception {
        when(service.update(bookFormatId, bookFormatRequest)).thenReturn(bookFormat);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookFormatRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookFormat)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при удалении формата книги, когда формат не найден по идентификатору
     */
    @Test
    public void DeleteThenWrongId_Returns404NotFound() throws Exception {
        Mockito.doThrow(BookFormatNotFoundException.class).when(service).delete(bookFormatId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное удаление формата книги
     */
    @Test
    public void Delete_Returns200OK() throws Exception {
        Mockito.doNothing().when(service).delete(bookFormatId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Book format with id: %d deleted", bookFormatId)))
                .andDo(print());
    }

}
