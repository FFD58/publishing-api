package ru.fafurin.publishing.dto.response.task;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTaskAddInfoResponse {
    UserTaskResponse task;
    String username;
    Integer orderNumber;
}
