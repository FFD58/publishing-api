package ru.fafurin.publishing.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderAllInfoResponse {
    //TODO: теоретически id не нужен
    private Long id;
    private Integer number;
    private String deadline;
    private String comment;
    private String status;
    private Long createdAt;
    private Long updatedAt;
    private Long finishedAt;
    private BookResponse book;
    private CustomerResponse customer;
    //TODO: в UserTaskResponse orderNumber не нужен
    private List<UserTaskResponse> tasks;
}
