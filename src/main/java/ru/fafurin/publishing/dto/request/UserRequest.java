package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Сотрудник")
public class UserRequest {
    @Schema(description = "Имя пользователя")
    @NotNull
    String username;

    @Schema(description = "Email")
    @NotNull
    @Email
    String email;

    @Schema(description = "Пароль")
    @NotNull
    @Length(min = 6)
    String password;

    @Schema(description = "Подтверждение пароля")
    @NotNull
    @Length(min = 6)
    String confirmPassword;

}
