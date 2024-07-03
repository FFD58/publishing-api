package ru.fafurin.publishing.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTaskResponse {
    private Long id;
    private String title;
    private String status;
    private String comment;
    private String username;
    private Integer orderNumber;
    private Long createdAt;
    private Long updatedAt;
    private Long finishedAt;
}
