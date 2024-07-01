package ru.fafurin.publishing.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.fafurin.publishing.entity.User;

import java.util.List;

public interface UserServiceContract {
    List<User> getAll();
    User get(Long id);
    User getByUsername(String username);
    void delete(Long id);
    User save(User user);
    UserDetailsService userDetailsService();
}
