package ru.fafurin.publishing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fafurin.publishing.exception.UserNotFoundException;
import ru.fafurin.publishing.entity.User;
import ru.fafurin.publishing.service.UserService;
import ru.fafurin.publishing.service.UserServiceContract;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Tag(
        name = "Сотрудники",
        description = "Методы для работы с сотрудниками"
)
public class UserController {

    @Autowired
    private UserServiceContract userService;

    @GetMapping
    @Operation(summary = "Получить информацию обо всех сотрудниках")
    public ResponseEntity<List<User>> list() {
        List<User> users = userService.getAll();
        if (users.isEmpty()) {
            log.info(String.valueOf(HttpStatus.NO_CONTENT));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о сотруднике по идентификатору")
    public ResponseEntity<User> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.get(id));
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
