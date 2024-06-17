package ru.fafurin.publishing.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.exception.UserTaskNotFoundException;
import ru.fafurin.publishing.model.Order;
import ru.fafurin.publishing.model.User;
import ru.fafurin.publishing.model.UserTask;
import ru.fafurin.publishing.repository.OrderRepository;
import ru.fafurin.publishing.repository.UserRepository;
import ru.fafurin.publishing.repository.UserTaskRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTaskServiceTest {

    @InjectMocks
    private UserTaskService service;

    @Mock
    private UserTaskRepository userTaskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    private UserTask userTask;
    private UserTaskRequest userTaskRequest;
    private Long userTaskId;

    @BeforeEach
    public void init() {
        userTaskId = 111L;
        userTask = UserTask.builder()
                .id(1L)
                .title("New Book Type")
                .user(Mockito.mock(User.class))
                .order(Mockito.mock(Order.class))
                .isDeleted(false).build();
        userTaskRequest = UserTaskRequest.builder()
                .title("New Book Type")
                .userId(1L)
                .orderId(1L)
                .build();
    }

    /**
     * Проверить получение всех задач
     */
    @Test
    public void GetAll_ReturnsList() {
        when(userTaskRepository.findAll()).thenReturn(List.of(userTask));

        List<UserTask> savedUserTasks = service.getAll();

        Assertions.assertFalse(savedUserTasks.isEmpty());

    }

    /**
     * Проверить сохранение задачи
     */
    @Test
    public void Save_ReturnsUserTask() {
        when(userTaskRepository.save(Mockito.any(UserTask.class))).thenReturn(userTask);

        Assertions.assertNotNull(service.save(userTaskRequest));
    }

    /**
     * Проверить получение задачи по идентификатору
     */
    @Test
    public void FindById_ReturnsUserTask() {
        when(userTaskRepository.findById(userTaskId)).thenReturn(Optional.ofNullable(userTask));

        Assertions.assertNotNull(service.get(userTaskId));
    }

    /**
     * Проверить исключение UserTaskNotFoundException если задача не найдена по идентификатору
     */
    @Test
    public void FindById_ReturnsUserTaskNotFoundException() {
        when(userTaskRepository.findById(userTaskId)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(UserTaskNotFoundException.class, () -> {
            service.get(userTaskId);
        });
        String expectedMessage = String.format("User task with id: %d not found", userTaskId);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Проверить изменение задачи по идентификатору
     */
    @Test
    public void UpdateById_ReturnsUserTask() {
        when(userTaskRepository.findById(userTaskId)).thenReturn(Optional.ofNullable(userTask));
        when(userTaskRepository.save(userTask)).thenReturn(userTask);

        Assertions.assertNotNull(service.update(userTaskId, userTaskRequest));
    }

    /**
     * Проверить безопасное удаление задачи по идентификатору
     */
    @Test
    public void SafeDeleteById_ReturnsVoid() {
        when(userTaskRepository.findById(userTaskId)).thenReturn(Optional.ofNullable(userTask));
        when(userTaskRepository.save(userTask)).thenReturn(userTask);

        service.delete(userTaskId);

        Assertions.assertTrue(userTask.isDeleted());
    }

}
