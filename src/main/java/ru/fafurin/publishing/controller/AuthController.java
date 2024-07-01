package ru.fafurin.publishing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fafurin.publishing.dto.request.LoginRequest;
import ru.fafurin.publishing.dto.request.SignUpRequest;
import ru.fafurin.publishing.dto.response.JwtResponse;
import ru.fafurin.publishing.service.AuthServiceContract;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthServiceContract authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtResponse signIn(@RequestBody @Valid LoginRequest request) {
        return authenticationService.logIn(request);
    }

    @Operation(summary = "Обновление токена")
    @PostMapping("/refresh")
    public JwtResponse refreshToken(@RequestBody @Valid LoginRequest request) {
        return authenticationService.logIn(request);
    }
}