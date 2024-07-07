package ru.fafurin.publishing.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.dto.response.task.UserTaskAddInfoResponse;
import ru.fafurin.publishing.entity.*;
import ru.fafurin.publishing.exception.UserTaskNotFoundException;
import ru.fafurin.publishing.repository.OrderRepository;
import ru.fafurin.publishing.repository.UserRepository;
import ru.fafurin.publishing.repository.UserTaskRepository;
import ru.fafurin.publishing.service.impl.UserTaskServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserTaskServiceTest {

    @InjectMocks
    private UserTaskServiceImpl service;

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
        Book book = Book.builder()
                .id(1L)
                .authors(List.of("Test Author"))
                .type(BookType.builder().id(1L).title("Test").build())
                .format(BookFormat.builder().id(1L).title("Test").designation("Test").build())
                .build();
        Order order = Order.builder()
                .id(1L)
                .book(book)
                .customer(Mockito.mock(Customer.class))
                .status(Status.AWAIT)
                .build();
        order.setStatus(Status.AWAIT);
        userTask = UserTask.builder()
                .id(1L)
                .title("New Book Type")
                .status(Status.AWAIT)
                .user(Mockito.mock(User.class))
                .order(order)
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

        when(userTaskRepository
                .findAll(Sort.by(Sort.Direction.DESC, "updatedAt")))
                .thenReturn(List.of(userTask));

        List<UserTaskAddInfoResponse> savedUserTasks = service.getAll();

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
        System.out.println(userTask.getOrder().getStatus());
        when(userTaskRepository.findById(userTaskId)).thenReturn(Optional.of(userTask));

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
