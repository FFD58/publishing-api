package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.SignUpRequest;
import ru.fafurin.publishing.dto.response.UserResponse;
import ru.fafurin.publishing.entity.User;

public class UserMapper {

    public static User getUser(User user, SignUpRequest userRequest) {
        user.setUsername(userRequest.getUsername());
        return user;
    }

    public static UserResponse getUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                //TODO: дописать функционал
                .phone("-")
                .email(user.getEmail())
                //TODO: дописать функционал
                .position("-")
                .build();
    }

}
