package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.SignUpRequest;
import ru.fafurin.publishing.model.User;

public class UserMapper {

    public static User getUser(User user, SignUpRequest userRequest) {
        user.setUsername(userRequest.getUsername());
        return user;
    }

}
