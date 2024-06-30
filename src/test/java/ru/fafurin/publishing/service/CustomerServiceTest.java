package ru.fafurin.publishing.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fafurin.publishing.dto.request.CustomerRequest;
import ru.fafurin.publishing.exception.CustomerNotFoundException;
import ru.fafurin.publishing.entity.Customer;
import ru.fafurin.publishing.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService service;

    @Mock
    private CustomerRepository repository;
    private Customer customer;
    private CustomerRequest customerRequest;
    private Long customerId;

    @BeforeEach
    public void init() {
        customerId = 111L;
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
    }

    /**
     * Проверить получение всех заказчиков
     */
    @Test
    public void GetAll_ReturnsList() {
       Customer customer1 = Customer.builder()
                .id(1L)
                .name("Another Customer")
                .phone("+71234567890")
                .email("another@customer.ru")
                .isDeleted(false).build();

        when(repository.findAll()).thenReturn(List.of(customer, customer1));

        List<Customer> savedCustomers = service.getAll();

        Assertions.assertFalse(savedCustomers.isEmpty());

    }

    /**
     * Проверить сохранение заказчика
     */
    @Test
    public void Save_ReturnsCustomer() {
        when(repository.save(Mockito.any(Customer.class))).thenReturn(customer);

        Assertions.assertNotNull(service.saveIfNotExists(customerRequest));
    }

    /**
     * Проверить получение заказчика по идентификатору
     */
    @Test
    public void FindById_ReturnsCustomer() {
        when(repository.findById(customerId)).thenReturn(Optional.ofNullable(customer));

        Assertions.assertNotNull(service.get(customerId));
    }

    /**
     * Проверить исключение CustomerNotFoundException если заказчик не найден по идентификатору
     */
    @Test
    public void FindById_ReturnsCustomerNotFoundException() {
        when(repository.findById(customerId)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(CustomerNotFoundException.class, () -> {
            service.get(customerId);
        });
        String expectedMessage = String.format("Customer with id: %d not found", customerId);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Проверить изменение заказчика по идентификатору
     */
    @Test
    public void UpdateById_ReturnsCustomer() {
        when(repository.findById(customerId)).thenReturn(Optional.ofNullable(customer));
        when(repository.save(customer)).thenReturn(customer);

        Assertions.assertNotNull(service.update(customerId, customerRequest));
    }

    /**
     * Проверить безопасное удаление заказчика по идентификатору
     */
    @Test
    public void SafeDeleteById_ReturnsVoid() {
        when(repository.findById(customerId)).thenReturn(Optional.ofNullable(customer));
        when(repository.save(customer)).thenReturn(customer);

        service.delete(customerId);

        Assertions.assertTrue(customer.isDeleted());
    }

}
