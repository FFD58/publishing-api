package ru.fafurin.publishing.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorMessage {
    private int status;
    private String message;
    private LocalDateTime createdAt;

    public ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
