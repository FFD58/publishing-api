package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Задача сотрудника")
public class UserTaskRequest {
    @Schema(description = "Название", example = "Первая читка")
    @Size(min = 3, max = 30)
    @NotEmpty
    String title;

    @Schema(description = "Комментарий")
    @Size(min = 3, max = 30)
    String comment;

    @Schema(description = "Идентификатор сотрудника")
    @NotNull
    Long userId;

    @Schema(description = "Идентификатор заказа")
    @NotNull
    Long orderId;
}
