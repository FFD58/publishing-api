package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Заказ")
public class OrderRequest {
    @Schema(description = "Номер",
            example = "123")
    private Integer number;

    @Schema(description = "Сроки исполнения",
            example = "Конец года")
    private String deadline;

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
