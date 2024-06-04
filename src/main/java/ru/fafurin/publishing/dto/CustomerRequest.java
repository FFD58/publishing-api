package ru.fafurin.publishing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    @Schema(description = "ФИО",
            example = "Иванов Иван Иванович")
    @NotNull
    private String name;
    @Schema(description = "Email",
            example = "ivanov@ivan.ru")
    @Email
    private String email;
    @Schema(description = "Номер телефона",
            example = "+71234567890")
    @NotNull
    private String phone;
}
