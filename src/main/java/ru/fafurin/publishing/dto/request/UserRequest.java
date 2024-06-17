package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @Schema(description = "Имя пользователя")
    @NotNull
    private String username;

    @Schema(description = "Email")
    @NotNull
    @Email
    private String email;

    @Schema(description = "Пароль")
    @NotNull
    @Length(min = 6)
    private String password;

    @Schema(description = "Подтверждение пароля")
    @NotNull
    @Length(min = 6)
    private String confirmPassword;

}
