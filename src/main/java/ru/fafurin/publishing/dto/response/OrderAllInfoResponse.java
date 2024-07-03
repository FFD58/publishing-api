package ru.fafurin.publishing.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderAllInfoResponse {
    private Long id;
    private Integer number;
    private LocalDateTime deadline;
    private String comment;
    private String status;
    private Long createdAt;
    private Long updatedAt;
    private Long finishedAt;
    private BookResponse book;
    private CustomerResponse customer;
    private List<UserTaskResponse> tasks;
}
