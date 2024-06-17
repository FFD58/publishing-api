package ru.fafurin.publishing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.request.SignInRequest;
import ru.fafurin.publishing.dto.request.SignUpRequest;
import ru.fafurin.publishing.dto.request.JwtResponse;
import ru.fafurin.publishing.model.Role;
import ru.fafurin.publishing.model.User;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtResponse signUp(SignUpRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.save(user);

        String token = jwtService.generateToken(user);
        return new JwtResponse(token, null);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        return new JwtResponse(token, refreshToken);
    }

    public JwtResponse refreshToken(String token) {
        JwtResponse jwtResponse = new JwtResponse();
        User user = userService.getByUsername(jwtService.extractUsername(token));
        if (jwtService.isTokenValid(token, user)) {
            jwtResponse.setToken(jwtService.generateToken(user));
            jwtResponse.setRefreshToken(token);
        }
        return jwtResponse;
    }
}
