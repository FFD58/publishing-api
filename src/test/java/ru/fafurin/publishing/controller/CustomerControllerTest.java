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
import ru.fafurin.publishing.dto.request.CustomerRequest;
import ru.fafurin.publishing.exception.CustomerNotFoundException;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.service.impl.CustomerServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private static final String END_POINT_PATH = "/api/v1/customers";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerServiceImpl service;
    private Customer customer;
    private CustomerRequest customerRequest;
    private String requestURI;
    private Long customerId;

    @BeforeEach
    public void init() {
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .phone("+71234567890")
                .email("test@customer.ru")
                .isDeleted(false).build();
        customerRequest = CustomerRequest.builder()
                .name("Test Customer")
                .phone("+71234567890")
                .email("test@customer.ru")
                .build();
        customerId = 111L;
        requestURI = END_POINT_PATH + "/" + customerId;
    }

    /**
     * Проверить статус Created при успешном сохранении нового заказчика
     */
    @Test
    public void Save_Returns201Created() throws Exception {
        when(service.save(customerRequest)).thenReturn(customer);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)))
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest в случае, если запрос не прошел валидацию
     */
    @Test
    public void SaveThenInvalidRequest_Returns400BadRequest() throws Exception {
        CustomerRequest invalidCustomerRequest = CustomerRequest.builder()
                .name(null).build();

        String requestBody = objectMapper.writeValueAsString(invalidCustomerRequest);
        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить статус NotFound в случае, если заказчик не найден по идентификатору
     */
    @Test
    public void GetByIdThenWrongId_Returns404NotFound() throws Exception {
        when(service.get(customerId)).thenThrow(CustomerNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное получение заказчика по идентификатору
     */
    @Test
    public void GetById_Returns200OK() throws Exception {
        when(service.get(customerId)).thenReturn(customer);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(customer)))
                .andDo(print());
    }

    /**
     * Проверить статус NoContent в случае, если вернется пустой список заказчиков
     */
    @Test
    public void GetAllThenEmptyList_Returns204NoContent() throws Exception {
        when(service.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Проверить успешное получение списка всех заказчиков
     */
    @Test
    public void GetAll_Returns200OK() throws Exception {
        List<Customer> customers = List.of(
                Mockito.mock(Customer.class),
                Mockito.mock(Customer.class),
                Mockito.mock(Customer.class)
        );

        when(service.getAll()).thenReturn(customers);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(customers)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при изменении заказчика, когда заказчик не найден по идентификатору
     */
    @Test
    public void UpdateThenWrongId_Returns404NotFound() throws Exception {
        when(service.update(customerId, customerRequest)).thenThrow(CustomerNotFoundException.class);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest при изменении заказчика, если запрос не прошел валидацию
     */
    @Test
    public void UpdateThenInvalidRequest_Returns400BadRequest() throws Exception {
        CustomerRequest invalidCustomerRequest = CustomerRequest.builder()
                .name(null).build();
        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidCustomerRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить успешное изменение заказчика
     */
    @Test
    public void Update_Returns200OK() throws Exception {
        when(service.update(customerId, customerRequest)).thenReturn(customer);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при удалении заказчика, когда заказчик не найден по идентификатору
     */
    @Test
    public void DeleteThenWrongId_Returns404NotFound() throws Exception {
        Mockito.doThrow(CustomerNotFoundException.class).when(service).delete(customerId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное удаление заказчика
     */
    @Test
    public void Delete_Returns200OK() throws Exception {
        Mockito.doNothing().when(service).delete(customerId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Customer with id: %d deleted", customerId)))
                .andDo(print());
    }

}
