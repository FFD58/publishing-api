package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Тип издания")
public class BookTypeRequest {
    @Schema(description = "Название", example = "Монография")
    @Size(min = 3, max = 30)
    @NotEmpty
    String title;
}
