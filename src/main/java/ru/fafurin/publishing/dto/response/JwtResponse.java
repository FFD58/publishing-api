package ru.fafurin.publishing.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Ответ c токеном доступа")
public class JwtResponse {
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;
    @Schema(description = "Токен для обновления", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String refreshToken;
    @Schema(description = "Имя пользователя")
    private String username;
    @Schema(description = "Роль пользователя", example = "ROLE_USER")
    private String role;
    @Schema(description = "Сообщение об успехе или ошибке")
    private String message;
    @Schema(description = "Срок действия токена")
    private int expirationTime;
}
