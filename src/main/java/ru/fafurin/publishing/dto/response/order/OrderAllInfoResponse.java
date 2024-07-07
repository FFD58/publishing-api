package ru.fafurin.publishing.dto.response.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.fafurin.publishing.dto.response.task.UserTaskAddInfoResponse;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAllInfoResponse {
    OrderAddInfoResponse order;
    List<UserTaskAddInfoResponse> tasks;
}
