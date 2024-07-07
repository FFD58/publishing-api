package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Заказ")
public class OrderRequest {
    @Schema(description = "Номер",
            example = "123")
    private Integer number;

    @Schema(description = "Сроки исполнения")
    private LocalDateTime deadline;

    @Schema(description = "Комментарий",
            example = "Цветные рисунки должны быть напечатаны в цвете")
    private String comment;

    @Schema(description = "Данные для сохранения книги")
    @NotNull
    private BookRequest book;

    @Schema(description = "Данные для сохранения заказчика")
    @NotNull
    private CustomerRequest customer;
}
