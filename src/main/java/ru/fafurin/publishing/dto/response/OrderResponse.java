package ru.fafurin.publishing.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.fafurin.publishing.dto.request.CustomerRequest;

@Data
@Builder
public class OrderResponse {
    private Integer number;
    private String deadline;
    private String comment;
    private String status;
    private Integer createdAt;
    private Integer updatedAt;
    private Integer finishedAt;
    private BookResponse book;
    private CustomerResponse customer;
}
