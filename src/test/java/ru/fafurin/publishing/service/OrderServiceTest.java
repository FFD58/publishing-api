package ru.fafurin.publishing.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.fafurin.publishing.dto.request.BookRequest;
import ru.fafurin.publishing.dto.request.CustomerRequest;
import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.order.OrderAddInfoResponse;
import ru.fafurin.publishing.entity.*;
import ru.fafurin.publishing.exception.OrderNotFoundException;
import ru.fafurin.publishing.repository.OrderRepository;
import ru.fafurin.publishing.service.impl.BookServiceImpl;
import ru.fafurin.publishing.service.impl.CustomerServiceImpl;
import ru.fafurin.publishing.service.impl.OrderServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl service;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MailService mailService;
    @Mock
    private BookServiceImpl bookServiceImpl;
    @Mock
    private CustomerServiceImpl customerService;
    private Order order;
    private OrderRequest orderRequest;
    private Long orderId;

    @BeforeEach
    public void init() {
        orderId = 111L;
        Book book = Book.builder()
                .id(1L)
                .authors(List.of("Test Author"))
                .type(BookType.builder().id(1L).title("Test").build())
                .format(BookFormat.builder().id(1L).title("Test").designation("Test").build())
                .build();
        order = Order.builder()
                .id(1L)
                .number(1)
                .deadline(LocalDateTime.now().plusDays(100))
                .comment("Comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .finishedAt(null)
                .book(book)
                .tasks(new ArrayList<>())
                .customer(Mockito.mock(Customer.class))
                .status(Status.AWAIT)
                .isDeleted(false)
                .build();
        orderRequest = OrderRequest.builder()
                .number(1)
                .deadline(LocalDateTime.now().plusDays(100))
                .comment("Comment")
                .book(Mockito.mock(BookRequest.class))
                .customer(Mockito.mock(CustomerRequest.class))
                .build();
    }

    /**
     * Проверить получение всех заказов
     */
    @Test
    public void GetAll_ReturnsList() {
        when(orderRepository
                .findAll(Sort.by(Sort.Direction.DESC, "updatedAt")))
                .thenReturn(List.of(order));

        List<OrderAddInfoResponse> savedOrders = service.getAwaitingOrders();

        Assertions.assertFalse(savedOrders.isEmpty());

    }

    /**
     * Проверить сохранение заказа
     */
    @Test
    public void Save_ReturnsOrder() {
        when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        when(bookServiceImpl.save(Mockito.any(BookRequest.class))).thenReturn(Mockito.mock(Book.class));
        when(customerService.save(Mockito.any(CustomerRequest.class))).thenReturn(Mockito.mock(Customer.class));
        Assertions.assertNotNull(service.save(orderRequest));
    }

    /**
     * Проверить получение заказа по идентификатору
     */
    @Test
    public void FindById_ReturnsOrder() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Assertions.assertNotNull(service.get(orderId));
    }

    /**
     * Проверить исключение OrderNotFoundException если заказ не найден по идентификатору
     */
    @Test
    public void FindById_ReturnsOrderNotFoundException() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(OrderNotFoundException.class, () -> {
            service.get(orderId);
        });
        String expectedMessage = String.format("Order with id: %d not found", orderId);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Проверить изменение заказа по идентификатору
     */
    @Test
    public void UpdateById_ReturnsOrder() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        Assertions.assertNotNull(service.update(orderId, orderRequest));
    }

    /**
     * Проверить безопасное удаление заказа по идентификатору
     */
    @Test
    public void SafeDeleteById_ReturnsVoid() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        service.delete(orderId);

        Assertions.assertTrue(order.isDeleted());
    }

}
