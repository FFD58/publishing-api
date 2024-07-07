package ru.fafurin.publishing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Ответ c токеном доступа")
public class JwtResponse {
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    String token;
    @Schema(description = "Токен для обновления", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    String refreshToken;
    @Schema(description = "Имя пользователя")
    String username;
    @Schema(description = "Роль пользователя", example = "ROLE_USER")
    String role;
    @Schema(description = "Сообщение об успехе или ошибке")
    String message;
    @Schema(description = "Срок действия токена")
    int expirationTime;
}
