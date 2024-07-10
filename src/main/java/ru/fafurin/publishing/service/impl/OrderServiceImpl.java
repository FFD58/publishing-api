package ru.fafurin.publishing.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.order.OrderAddInfoResponse;
import ru.fafurin.publishing.dto.response.order.OrderAllInfoResponse;
import ru.fafurin.publishing.entity.*;
import ru.fafurin.publishing.exception.OrderNotFoundException;
import ru.fafurin.publishing.mapper.OrderMapper;
import ru.fafurin.publishing.repository.BookFileRepository;
import ru.fafurin.publishing.repository.OrderRepository;
import ru.fafurin.publishing.service.FileService;
import ru.fafurin.publishing.service.MailService;
import ru.fafurin.publishing.service.OrderService;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BookFileRepository fileRepository;
    private final BookServiceImpl bookServiceImpl;
    private final CustomerServiceImpl customerService;
    private final MailService mailService;
    private final FileService fileService;

    /**
     * Получить список всех заказов со статусом AWAIT, отсортированных по дате изменения
     *
     * @return список всех заказов со статусом AWAIT, отсортированных по дате изменения
     */
    @Override
    public List<OrderAddInfoResponse> getAwaitingOrders() {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));

        return orders.stream()
                .filter(o -> !o.isDeleted())
                .filter(o -> o.getStatus() == Status.AWAIT)
                .map(OrderMapper::getOrderAddInfoResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получить полную информацию о заказе по идентификатору
     *
     * @param id - идентификатор заказа
     * @return полная информация о заказе или выбрасывается исключение,
     * если заказ не найден по идентификатору
     */
    @Override
    public OrderAllInfoResponse get(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            return OrderMapper.getOrderAllInfoResponse(orderOptional.get());
        } else throw new OrderNotFoundException(id);
    }

    /**
     * Сохранить нового заказа
     *
     * @param orderRequest - данные для сохранения нового заказа
     * @return сохраненный заказ
     */
    @Override
    public Order save(OrderRequest orderRequest) {
        Order order = OrderMapper.getOrder(new Order(), orderRequest);

        Book book = bookServiceImpl.save(orderRequest.getBook());
        Customer customer = customerService.save(orderRequest.getCustomer());
        order.setBook(book);
        order.setCustomer(customer);
        order.setStatus(Status.AWAIT);
        orderRepository.save(order);
        mailService.sendEmail(order.getCustomer(), MailType.CREATION, null);
        return order;
    }

    /**
     * Изменить данные существующего заказа
     *
     * @param id           - идентификатор заказа
     * @param orderRequest - данные для изменения существующего заказа
     * @return - измененный заказ или выбрасывается исключение,
     * если заказ не найден по идентификатору
     */
    @Override
    public Order update(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        Long bookId = order.getBook().getId();
        bookServiceImpl.update(bookId, orderRequest.getBook());
        Long customerId = order.getCustomer().getId();
        customerService.update(customerId, orderRequest.getCustomer());
        return orderRepository.save(
                OrderMapper.getOrder(order, orderRequest));
    }

    /**
     * Безопасно удалить заказа по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     *
     * @param id - идентификатор заказа
     */
    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        order.setDeleted(true);
        Long bookId = order.getBook().getId();
        bookServiceImpl.delete(bookId);
        orderRepository.save(order);
    }

    /**
     * Получить список заказов, у которых дедлайн наступает
     * в интервале от сегодняшнего дня + duration
     *
     * @param duration - продолжительность
     * @return список заказов
     */
    @Override
    public List<Order> findAllSoonOrders(Duration duration) {
        LocalDateTime now = LocalDateTime.now();
        return orderRepository.findAllSoonOrders(
                Timestamp.valueOf(now),
                Timestamp.valueOf(now.plus(duration)));
    }

    public void refresh(Order order) {
        orderRepository.save(order);
    }

    /**
     * Загрузить файл
     *
     * @param id   - идентификатор заказа
     * @param file - файл
     * @return - название файла
     */
    @Override
    public String uploadFile(Long id, MultipartFile file) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        String storageFileName = fileService.storeFile(file, LocalDateTime.now());
        BookFile bookFile = BookFile.builder()
                .path(storageFileName)
                .type(file.getContentType())
                .build();
        System.out.println(bookFile);
        fileRepository.save(bookFile);
        order.getBook().setFiles(List.of(bookFile));
        orderRepository.save(order);
        return storageFileName;
    }
}
