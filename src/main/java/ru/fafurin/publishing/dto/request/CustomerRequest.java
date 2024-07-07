package ru.fafurin.publishing.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Заказчик")
public class CustomerRequest {
    @Schema(description = "ФИО",
            example = "Иванов Иван Иванович")
    @NotEmpty(message = "Name must not be empty")
    String name;

    @Schema(description = "Email",
            example = "ivanov@ivan.ru")
    @Email
    @NotEmpty(message = "Email must not be empty")
    String email;

    @Schema(description = "Номер телефона",
            example = "+71234567890")
    @NotEmpty(message = "Phone must not be empty")
    String phone;
}
