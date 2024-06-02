package ru.fafurin.publishing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotEmpty
    private String title;
    @NotNull
    private Long bookTypeId;
    @NotNull
    private Long bookFormatId;
    @NotEmpty
    private List<String> authors;
    private List<String> files;
    @NotNull
    private Long orderId;
}
