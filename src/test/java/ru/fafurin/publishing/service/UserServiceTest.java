package ru.fafurin.publishing.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fafurin.publishing.dto.request.UserRequest;
import ru.fafurin.publishing.entity.User;
import ru.fafurin.publishing.exception.UserNotFoundException;
import ru.fafurin.publishing.repository.UserRepository;
import ru.fafurin.publishing.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository userRepository;
    private User user;
    private UserRequest userRequest;
    private Long userId;

    @BeforeEach
    public void init() {
        userId = 111L;
        user = User.builder()
                .id(1L)
                .email("test@test.ru")
                .username("Test")
                .password("Test")
                .isDeleted(false)
                .build();
    }

    /**
     * Проверить получение всех сотрудников
     */
    @Test
    public void GetAll_ReturnsList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> savedUsers = service.getAll();

        Assertions.assertFalse(savedUsers.isEmpty());

    }

    /**
     * Проверить получение сотрудника по идентификатору
     */
    @Test
    public void FindById_ReturnsUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Assertions.assertNotNull(service.get(userId));
    }

    /**
     * Проверить исключение UserNotFoundException если сотрудник не найден по идентификатору
     */
    @Test
    public void FindById_ReturnsUserNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            service.get(userId);
        });
        String expectedMessage = String.format("User with id: %d not found", userId);
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    /**
     * Проверить безопасное удаление сотрудника по идентификатору
     */
    @Test
    public void SafeDeleteById_ReturnsVoid() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        service.delete(userId);
        Assertions.assertTrue(user.isDeleted());
    }
}
