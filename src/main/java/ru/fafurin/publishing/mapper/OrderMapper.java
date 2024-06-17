package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.OrderResponse;
import ru.fafurin.publishing.model.Order;

public class OrderMapper {

    public static Order getOrder(Order order, OrderRequest orderRequest) {
        order.setNumber(orderRequest.getNumber());
        order.setComment(orderRequest.getComment());
        order.setDeadline(orderRequest.getDeadline());
        return order;
    }

    public static OrderResponse getOrderResponse(Order order) {
        return OrderResponse.builder()
                .number(order.getNumber())
                .comment(order.getComment())
                .deadline(order.getDeadline())
                .status(order.getStatus().name())
                .book(BookMapper.getBookResponse(order.getBook()))
                .customer(CustomerMapper.getCustomerResponse(order.getCustomer()))
                .build();
    }

}
