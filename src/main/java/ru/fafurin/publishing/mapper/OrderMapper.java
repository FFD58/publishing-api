package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.order.OrderAddInfoResponse;
import ru.fafurin.publishing.dto.response.order.OrderAllInfoResponse;
import ru.fafurin.publishing.dto.response.order.OrderResponse;
import ru.fafurin.publishing.entity.Order;

public class OrderMapper {

    public static Order getOrder(Order order, OrderRequest orderRequest) {
        order.setNumber(orderRequest.getNumber());
        order.setComment(orderRequest.getComment());
        order.setDeadline(orderRequest.getDeadline());
        return order;
    }

    public static OrderResponse getOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .number(order.getNumber())
                .deadline(order.getDeadline())
                .comment(order.getComment())
                .status(order.getStatus().getTitle())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .finishedAt(order.getFinishedAt())
                .build();
    }

    public static OrderAddInfoResponse getOrderAddInfoResponse(Order order) {
        return OrderAddInfoResponse.builder()
                .order(getOrderResponse(order))
                .book(BookMapper.getBookResponse(order.getBook()))
                .customer(CustomerMapper.getCustomerResponse(order.getCustomer()))
                .build();
    }

    public static OrderAllInfoResponse getOrderAllInfoResponse(Order order) {
        return OrderAllInfoResponse.builder()
                .order(getOrderAddInfoResponse(order))
                .tasks(order.getTasks().stream()
                        .map(UserTaskMapper::getUserTaskAddInfoResponse)
                        .toList())
                .build();
    }

}
