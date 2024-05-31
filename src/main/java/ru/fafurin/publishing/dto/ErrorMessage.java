package ru.fafurin.publishing.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorMessage {
    private String message;
    private LocalDateTime createdAt;

    public ErrorMessage(String message) {
        this.createdAt = LocalDateTime.now();
        this.message = message;
    }
}
