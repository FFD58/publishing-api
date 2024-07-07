package ru.fafurin.publishing.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorMessage {
    int status;
    String message;
    LocalDateTime createdAt;

    public ErrorMessage(int status, String message) {
        this.status = status;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
