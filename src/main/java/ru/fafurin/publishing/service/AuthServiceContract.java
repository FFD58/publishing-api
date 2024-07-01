package ru.fafurin.publishing.service;

import ru.fafurin.publishing.dto.request.LoginRequest;
import ru.fafurin.publishing.dto.request.SignUpRequest;
import ru.fafurin.publishing.dto.response.JwtResponse;

public interface AuthServiceContract {
    JwtResponse signUp(SignUpRequest request);
    JwtResponse logIn(LoginRequest request);
    JwtResponse refreshToken(String token);
}
