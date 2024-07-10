package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Книга")
public class BookRequest {
    @Schema(description = "Название",
            example = "Война и мир")
    @Size(min = 3, max = 50)
    @NotEmpty(message = "Title must be not null")
    String title;

    @Schema(description = "Идентификатор типа книги",
            example = "1")
    @NotNull(message = "Book type id must be not null")
    Long typeId;

    @Schema(description = "Идентификатор формата книги",
            example = "1")
    @NotNull(message = "Book format id must be not null")
    Long formatId;

    @Schema(description = "ФИО автора/ов",
            example = "Толстой Лев Николаевич")
    @NotEmpty(message = "Authors must be not null")
    String authors;
}
