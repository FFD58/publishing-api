package ru.fafurin.publishing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Книга")
public class BookRequest {
    @Schema(description = "Название",
            example = "Война и мир")
    @Size(min = 3, max = 50)
    @NotBlank
    private String title;
    private Long bookTypeId;
    private Long bookFormatId;
    private List<String> authors;
    private List<String> files;
    private Long orderId;
}
