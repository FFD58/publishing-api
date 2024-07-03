package ru.fafurin.publishing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.request.LoginRequest;
import ru.fafurin.publishing.dto.request.SignUpRequest;
import ru.fafurin.publishing.dto.response.JwtResponse;
import ru.fafurin.publishing.entity.Role;
import ru.fafurin.publishing.entity.User;
import ru.fafurin.publishing.service.AuthService;
import ru.fafurin.publishing.service.JwtService;
import ru.fafurin.publishing.service.UserService;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${token.lifetime}")
    private Integer lifetime;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return DTO токена
     */
    @Override
    public JwtResponse signUp(SignUpRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        System.out.println("!!!!" + user);

        userService.save(user);


        return JwtResponse.builder()
                .token(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), user))
                .username(user.getUsername())
                .role(user.getRole().name())
                .expirationTime(lifetime)
                .message("Пользователь зарегистрирован")
                .build();
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return DTO токена
     */
    @Override
    public JwtResponse logIn(LoginRequest request) {
        JwtResponse jwtResponse = new JwtResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));

            UserDetails user = userService
                    .userDetailsService()
                    .loadUserByUsername(request.getUsername());

            String role = user.getAuthorities().stream().findFirst().get().toString();

            jwtResponse = JwtResponse.builder()
                    .token(jwtService.generateToken(user))
                    .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), user))
                    .username(user.getUsername())
                    .role(role)
                    .expirationTime(lifetime)
                    .message("Пользователь аутентифицирован")
                    .build();

            System.out.println(jwtResponse);

        } catch (RuntimeException e) {
            jwtResponse.setMessage(e.getMessage());
        }
        System.out.println(jwtResponse);
        return jwtResponse;
    }

    /**
     * Обновление токена
     *
     * @param token - токен
     * @return DTO токена
     */
    @Override
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
