package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.OrderRequest;
import ru.fafurin.publishing.model.Order;

public class OrderMapper {

    public static Order getOrder(Order order, OrderRequest orderRequest) {
        order.setNumber(orderRequest.getNumber());
        order.setComment(orderRequest.getComment());
        order.setDeadline(orderRequest.getDeadline());
        return order;
    }
}
