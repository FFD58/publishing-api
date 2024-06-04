package ru.fafurin.publishing.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.OrderRequest;
import ru.fafurin.publishing.exception.OrderNotFoundException;
import ru.fafurin.publishing.mapper.OrderMapper;
import ru.fafurin.publishing.model.Book;
import ru.fafurin.publishing.model.Customer;
import ru.fafurin.publishing.model.Order;
import ru.fafurin.publishing.model.Status;
import ru.fafurin.publishing.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookService bookService;
    private final CustomerService customerService;

    /**
     * Получить список всех заказов
     * @return список всех заказов
     */
    public List<Order> getAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Получить заказа по идентификатору
     * @param id - идентификатор заказа
     * @return заказ или выбрасывается исключение,
     *         если заказ не найден по идентификатору
     */
    public Order get(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Сохранить нового заказа
     * @param orderRequest - данные для сохранения нового заказа
     * @return сохраненный заказ
     */
    public Order save(OrderRequest orderRequest) {
        Order order = OrderMapper.getOrder(new Order(), orderRequest);

        Book book = bookService.save(orderRequest.getBook());
        Customer customer = customerService.saveIfNotExists(orderRequest.getCustomer());
        order.setBook(book);
        order.setCustomer(customer);
        order.setStatus(Status.AWAIT);
        return orderRepository.save(order);
    }

    /**
     * Изменить данные существующего заказа
     * @param id - идентификатор заказа
     * @param orderRequest - данные для изменения существующего заказа
     * @return - измененный заказ или выбрасывается исключение,
     *           если заказ не найден по идентификатору
     */
    public Order update(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        Long bookId = order.getBook().getId();
        bookService.update(bookId, orderRequest.getBook());
        Long customerId = order.getCustomer().getId();
        customerService.update(customerId, orderRequest.getCustomer());
        return orderRepository.save(
                OrderMapper.getOrder(order, orderRequest));
    }

    /**
     * Безопасно удалить заказа по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     * @param id - идентификатор заказа
     */
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        order.setDeleted(true);
        Long bookId = order.getBook().getId();
        bookService.delete(bookId);
        orderRepository.save(order);
    }

}
