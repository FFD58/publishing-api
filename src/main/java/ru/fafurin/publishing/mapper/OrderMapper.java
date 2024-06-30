package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.OrderRequest;
import ru.fafurin.publishing.dto.response.OrderAllInfoResponse;
import ru.fafurin.publishing.dto.response.OrderResponse;
import ru.fafurin.publishing.entity.Order;
import ru.fafurin.publishing.util.DateTimeUtil;

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
                .status(order.getStatus().getTitle())
                .book(BookMapper.getBookResponse(order.getBook()))
                .customer(CustomerMapper.getCustomerResponse(order.getCustomer()))
                .createdAt(DateTimeUtil.toTimestamp(order.getCreatedAt()))
                .updatedAt(DateTimeUtil.toTimestamp(order.getUpdatedAt()))
                .finishedAt(DateTimeUtil.toTimestamp(order.getFinishedAt()))
                .build();
    }

    public static OrderAllInfoResponse getOrderAllInfoResponse(Order order) {
        return OrderAllInfoResponse.builder()
                .id(order.getId())
                .number(order.getNumber())
                .deadline(order.getDeadline())
                .comment(order.getComment())
                .status(order.getStatus().getTitle())
                .book(BookMapper.getBookResponse(order.getBook()))
                .customer(CustomerMapper.getCustomerResponse(order.getCustomer()))
                .tasks(order.getTasks().stream().map(UserTaskMapper::getUserTaskResponse).toList())
                .createdAt(DateTimeUtil.toTimestamp(order.getCreatedAt()))
                .updatedAt(DateTimeUtil.toTimestamp(order.getUpdatedAt()))
                .finishedAt(DateTimeUtil.toTimestamp(order.getFinishedAt()))
                .build();
    }

}
