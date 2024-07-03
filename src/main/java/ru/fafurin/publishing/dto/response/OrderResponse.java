package ru.fafurin.publishing.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Integer number;
    private LocalDateTime deadline;
    private String status;
    private Long createdAt;
    private Long updatedAt;
    private Long finishedAt;
    private BookResponse book;
    private CustomerResponse customer;
}
