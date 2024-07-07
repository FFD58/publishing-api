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
@Schema(description = "Книжный формат")
public class BookFormatRequest {
    @Schema(description = "Название", example = "А4")
    @Size(min = 2, max = 30)
    @NotEmpty
    String title;

    @Schema(description = "Размер", example = "60х84/8")
    String designation;
}
