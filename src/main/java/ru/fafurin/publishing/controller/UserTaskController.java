package ru.fafurin.publishing.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.dto.response.UserTaskAllInfoResponse;
import ru.fafurin.publishing.dto.response.UserTaskResponse;
import ru.fafurin.publishing.exception.UserTaskNotFoundException;
import ru.fafurin.publishing.entity.UserTask;
import ru.fafurin.publishing.service.UserTaskService;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/tasks")
@Tag(
        name = "Задачи сотрудников",
        description = "Методы для работы с задачами сотрудников"
)
public class UserTaskController {

    private final UserTaskService userTaskService;
    private final Counter addTaskCounter = Metrics.counter("add_task_count");

    @GetMapping
    @Operation(summary = "Получить информацию обо всех задачах сотрудников")
    public ResponseEntity<List<UserTaskResponse>> list() {
        List<UserTaskResponse> userTasks = userTaskService.getAll();
        if (userTasks.isEmpty()) {
            log.info(String.valueOf(HttpStatus.NO_CONTENT));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userTasks);
    }

    @GetMapping("/current-user")
    @Operation(summary = "Получить информацию обо всех задачах залогиненного сотрудника")
    public ResponseEntity<List<UserTaskResponse>> getCurrentUserTasks(Principal principal) {
        List<UserTaskResponse> userTasks = userTaskService.getAllByUser(principal);
        if (userTasks.isEmpty()) {
            log.info(String.valueOf(HttpStatus.NO_CONTENT));
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userTasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о задаче по идентификатору")
    public ResponseEntity<UserTaskAllInfoResponse> get(@PathVariable("id") Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userTaskService.get(id));
        } catch (UserTaskNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/complete/{id}")
    @Operation(summary = "Завершить задачу по идентификатору")
    public ResponseEntity<String> complete(@PathVariable("id") Long id) {
        try {
            userTaskService.complete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Задача успешно выполнена");
        } catch (UserTaskNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(summary = "Сохранить новую задачу")
    public ResponseEntity<UserTask> save(
            @RequestBody @Valid UserTaskRequest userTaskRequest) {

        addTaskCounter.increment();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userTaskService.save(userTaskRequest));
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Изменить задачу по идентификатору")
    public ResponseEntity<UserTask> updateUserTask(
            @RequestBody @Valid UserTaskRequest userTaskRequest,
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userTaskService.update(id, userTaskRequest));
        } catch (UserTaskNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу по идентификатору")
    public ResponseEntity<String> deleteUserTask(
            @Parameter(description = "Идентификатор") @PathVariable Long id) {
        try {
            userTaskService.delete(id);
            return ResponseEntity.ok(String.format("User task with id: %d deleted", id));
        } catch (UserTaskNotFoundException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
