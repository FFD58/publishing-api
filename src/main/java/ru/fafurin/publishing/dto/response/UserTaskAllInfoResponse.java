package ru.fafurin.publishing.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTaskAllInfoResponse {
    private Long id;
    private String title;
    private String status;
    private String comment;
    private Long createdAt;
    private Long updatedAt;
    private Long finishedAt;
    private OrderResponse order;
    private UserResponse user;
}
