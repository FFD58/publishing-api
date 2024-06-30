package ru.fafurin.publishing.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookResponse {
    private String title;
    private String type;
    private String format;
    private String authors;
}
