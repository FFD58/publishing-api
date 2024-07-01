package ru.fafurin.publishing.service;

import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.OrderAllInfoResponse;
import ru.fafurin.publishing.dto.response.OrderResponse;
import ru.fafurin.publishing.entity.Order;

import java.util.List;

public interface OrderServiceContract {
    List<OrderResponse> getAwaitingOrders();

    OrderAllInfoResponse get(Long id);

    Order save(OrderRequest orderRequest);

    Order update(Long id, OrderRequest orderRequest);

    void delete(Long id);
}
