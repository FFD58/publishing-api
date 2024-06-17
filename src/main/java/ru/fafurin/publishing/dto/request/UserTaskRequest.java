package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Задача сотрудника")
public class UserTaskRequest {
    @Schema(description = "Название", example = "Первая читка")
    @Size(min = 3, max = 30)
    @NotEmpty
    private String title;

    @Schema(description = "Идентификатор сотрудника")
    @NotNull
    private Long userId;

    @Schema(description = "Идентификатор заказа")
    @NotNull
    private Long orderId;
}
