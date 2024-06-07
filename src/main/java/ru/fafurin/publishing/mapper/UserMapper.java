package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.JwtRequest;
import ru.fafurin.publishing.model.User;

public class UserMapper {

    public static User getUser(User user, JwtRequest userRequest) {
        user.setUsername(userRequest.getUsername());
        return user;
    }

}
