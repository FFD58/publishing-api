package ru.fafurin.publishing.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.UserTaskRequest;
import ru.fafurin.publishing.exception.UserTaskNotFoundException;
import ru.fafurin.publishing.mapper.UserTaskMapper;
import ru.fafurin.publishing.model.Order;
import ru.fafurin.publishing.model.Status;
import ru.fafurin.publishing.model.User;
import ru.fafurin.publishing.model.UserTask;
import ru.fafurin.publishing.repository.OrderRepository;
import ru.fafurin.publishing.repository.UserRepository;
import ru.fafurin.publishing.repository.UserTaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserTaskService {

    private final UserTaskRepository userTaskRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * Получить список всех задач
     * @return список всех задач
     */
    public List<UserTask> getAll() {
        List<UserTask> userTasks = userTaskRepository.findAll();
        return userTasks.stream()
                .filter(b -> !b.isDeleted())
                .collect(Collectors.toList());
    }

    /**
     * Получить задачу по идентификатору
     * @param id - идентификатор задачи
     * @return задачу или выбрасывается исключение,
     *         если задача не найдена по идентификатору
     */
    public UserTask get(Long id) {
        return userTaskRepository.findById(id)
                .orElseThrow(() -> new UserTaskNotFoundException(id));
    }

    /**
     * Сохранить новую задачу
     * @param userTaskRequest - данные для сохранения новой задачи
     * @return сохраненная задача
     */
    public UserTask save(UserTaskRequest userTaskRequest) {
        UserTask userTask = UserTaskMapper.getUserTask(new UserTask(), userTaskRequest);

        Optional<User> userOptional = userRepository.findById(userTaskRequest.getUserId());
        userOptional.ifPresent(userTask::setUser);

        Optional<Order> orderOptional = orderRepository.findById(userTaskRequest.getOrderId());
        orderOptional.ifPresent(userTask::setOrder);

        userTask.setStatus(Status.IN_WORK);
        return userTaskRepository.save(userTask);
    }

    /**
     * Изменить данные существующей задачи
     * @param id - идентификатор задачи
     * @param userTaskRequest - данные для изменения существующей задачи
     * @return - измененная задача или выбрасывается исключение,
     *           если задача не найдена по идентификатору
     */
    public UserTask update(Long id, UserTaskRequest userTaskRequest) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new UserTaskNotFoundException(id));

        return userTaskRepository.save(
                UserTaskMapper.getUserTask(userTask, userTaskRequest));
    }

    /**
     * Безопасно удалить задачу по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     * @param id - идентификатор задачи
     */
    public void delete(Long id) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new UserTaskNotFoundException(id));
        userTask.setDeleted(true);
        userTaskRepository.save(userTask);
    }

}
