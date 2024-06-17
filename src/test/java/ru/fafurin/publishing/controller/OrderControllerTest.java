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
import ru.fafurin.publishing.dto.request.CustomerRequest;
import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.exception.OrderNotFoundException;
import ru.fafurin.publishing.model.*;
import ru.fafurin.publishing.service.OrderService;

import java.time.LocalDateTime;
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
public class OrderControllerTest {

    private static final String END_POINT_PATH = "/api/v1/orders";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService service;
    private Order order;
    private OrderRequest orderRequest;
    private String requestURI;
    private Long orderId;

    @BeforeEach
    public void init() {
        Book book = Book.builder()
                .title("Test Book")
                .type(Mockito.mock(BookType.class))
                .format(Mockito.mock(BookFormat.class))
                .authors(List.of("Test Author"))
                .files(List.of(Mockito.mock(BookFile.class)))
                .build();
        BookRequest bookRequest = BookRequest.builder()
                .title("Test Book")
                .typeId(1L)
                .formatId(1L)
                .authors(List.of("Test Author"))
                .files(List.of("test.file"))
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .phone("+71234567890")
                .email("test@customer.ru")
                .isDeleted(false).build();
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name("Test Customer")
                .phone("+71234567890")
                .email("test@customer.ru")
                .build();

        order = Order.builder()
                .id(1L)
                .number(1)
                .deadline("Deadline")
                .comment("Comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .finishedAt(null)
                .book(book)
                .customer(customer)
                .status(Status.AWAIT)
                .isDeleted(false)
                .build();
        orderRequest = OrderRequest.builder()
                .number(1)
                .deadline("Deadline")
                .comment("Comment")
                .book(bookRequest)
                .customer(customerRequest)
                .build();

        orderId = 111L;
        requestURI = END_POINT_PATH + "/" + orderId;
    }

    /**
     * Проверить статус Created при успешном сохранении нового заказа
     */
    @Test
    public void Save_Returns201Created() throws Exception {
        when(service.save(orderRequest)).thenReturn(order);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(order)))
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest в случае, если запрос не прошел валидацию
     */
    @Test
    public void SaveThenInvalidRequest_Returns400BadRequest() throws Exception {
        OrderRequest invalidOrderRequest = OrderRequest.builder().build();

        String requestBody = objectMapper.writeValueAsString(invalidOrderRequest);
        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить статус NotFound в случае, если заказ не найден по идентификатору
     */
    @Test
    public void GetByIdThenWrongId_Returns404NotFound() throws Exception {
        when(service.get(orderId)).thenThrow(OrderNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное получение заказа по идентификатору
     */
    @Test
    public void GetById_Returns200OK() throws Exception {
        when(service.get(orderId)).thenReturn(order);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(order)))
                .andDo(print());
    }

    /**
     * Проверить статус NoContent в случае, если вернется пустой список заказов
     */
    @Test
    public void GetAllThenEmptyList_Returns204NoContent() throws Exception {
        when(service.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Проверить успешное получение списка всех заказов
     */
    @Test
    public void GetAll_Returns200OK() throws Exception {
        List<Order> orders = List.of(
                Mockito.mock(Order.class),
                Mockito.mock(Order.class),
                Mockito.mock(Order.class)
        );

        when(service.getAll()).thenReturn(orders);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(orders)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при изменении заказа, когда заказ не найден по идентификатору
     */
    @Test
    public void UpdateThenWrongId_Returns404NotFound() throws Exception {
        when(service.update(orderId, orderRequest)).thenThrow(OrderNotFoundException.class);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest при изменении заказа, если запрос не прошел валидацию
     */
    @Test
    public void UpdateThenInvalidRequest_Returns400BadRequest() throws Exception {
        OrderRequest invalidOrderRequest = OrderRequest.builder().build();
        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidOrderRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить успешное изменение заказа
     */
    @Test
    public void Update_Returns200OK() throws Exception {
        when(service.update(orderId, orderRequest)).thenReturn(order);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при удалении заказа, когда заказ не найден по идентификатору
     */
    @Test
    public void DeleteThenWrongId_Returns404NotFound() throws Exception {
        Mockito.doThrow(OrderNotFoundException.class).when(service).delete(orderId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное удаление заказа
     */
    @Test
    public void Delete_Returns200OK() throws Exception {
        Mockito.doNothing().when(service).delete(orderId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Order with id: %d deleted", orderId)))
                .andDo(print());
    }

}
