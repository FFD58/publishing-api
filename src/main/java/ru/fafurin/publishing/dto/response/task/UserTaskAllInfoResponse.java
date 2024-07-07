package ru.fafurin.publishing.dto.response.task;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.fafurin.publishing.dto.response.UserResponse;
import ru.fafurin.publishing.dto.response.order.OrderAddInfoResponse;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserTaskAllInfoResponse {
    UserTaskResponse task;
    OrderAddInfoResponse order;
    UserResponse user;
}
