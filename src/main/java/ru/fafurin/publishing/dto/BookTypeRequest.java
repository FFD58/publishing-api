package ru.fafurin.publishing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Тип издания")
public class BookTypeRequest {
    @Schema(description = "Название", example = "Монография")
    @Size(min = 3, max = 30)
    @NotEmpty
    private String title;
}
