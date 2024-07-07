package ru.fafurin.publishing.service;

import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.order.OrderAllInfoResponse;
import ru.fafurin.publishing.dto.response.order.OrderAddInfoResponse;
import ru.fafurin.publishing.entity.Order;

import java.time.Duration;
import java.util.List;

public interface OrderService {
    List<OrderAddInfoResponse> getAwaitingOrders();

    OrderAllInfoResponse get(Long id);

    Order save(OrderRequest orderRequest);

    Order update(Long id, OrderRequest orderRequest);

    void delete(Long id);

    List<Order> findAllSoonOrders(Duration duration);

    void refresh(Order order);

}
