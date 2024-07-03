package ru.fafurin.publishing.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.dto.response.UserTaskAllInfoResponse;
import ru.fafurin.publishing.dto.response.UserTaskResponse;
import ru.fafurin.publishing.exception.UserNotFoundException;
import ru.fafurin.publishing.exception.UserTaskNotFoundException;
import ru.fafurin.publishing.mapper.UserTaskMapper;
import ru.fafurin.publishing.entity.Order;
import ru.fafurin.publishing.entity.Status;
import ru.fafurin.publishing.entity.User;
import ru.fafurin.publishing.entity.UserTask;
import ru.fafurin.publishing.repository.OrderRepository;
import ru.fafurin.publishing.repository.UserRepository;
import ru.fafurin.publishing.repository.UserTaskRepository;
import ru.fafurin.publishing.service.UserTaskService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserTaskServiceImpl implements UserTaskService {

    private final UserTaskRepository userTaskRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * Получить список всех задач, отсортированных по дате изменения
     *
     * @return список всех задач, отсортированных по дате изменения
     */
    @Override
    public List<UserTaskResponse> getAll() {
        List<UserTask> userTasks = userTaskRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
        return userTasks.stream()
                .filter(t -> !t.isDeleted())
                .map(UserTaskMapper::getUserTaskResponse)
                .collect(Collectors.toList());
    }

    /**
     * Получить полную информацию о задаче по идентификатору
     *
     * @param id - идентификатор задачи
     * @return полная информация о задаче или выбрасывается исключение,
     * если задача не найдена по идентификатору
     */
    @Override
    public UserTaskAllInfoResponse get(Long id) {
        Optional<UserTask> userTaskOptional = userTaskRepository.findById(id);
        if (userTaskOptional.isPresent()) {
            return UserTaskMapper.getUserTaskAllInfoResponse(userTaskOptional.get());
        } else throw new UserTaskNotFoundException(id);
    }


    /**
     * Получить список задач залогиненного пользователя
     *
     * @param principal - объект представляющий доступ к данным залогиненного пользователя
     * @return - список задач залогиненного пользователя
     */
    @Override
    public List<UserTaskResponse> getAllByUser(Principal principal) {
        Optional<User> userOptional = userRepository.findByUsername(principal.getName());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<UserTask> tasks = user.getTasks();
            return tasks.stream()
                    .filter(t -> !t.isDeleted())
                    .map(UserTaskMapper::getUserTaskResponse)
                    .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException(principal.getName());
        }
    }

    /**
     * Сохранить новую задачу
     *
     * @param userTaskRequest - данные для сохранения новой задачи
     * @return сохраненная задача
     */
    @Override
    public UserTask save(UserTaskRequest userTaskRequest) {

        System.out.println(userTaskRequest);

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
     *
     * @param id              - идентификатор задачи
     * @param userTaskRequest - данные для изменения существующей задачи
     * @return - измененная задача или выбрасывается исключение,
     * если задача не найдена по идентификатору
     */
    @Override
    public UserTask update(Long id, UserTaskRequest userTaskRequest) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new UserTaskNotFoundException(id));

        return userTaskRepository.save(
                UserTaskMapper.getUserTask(userTask, userTaskRequest));
    }

    /**
     * Безопасно удалить задачу по идентификатору,
     * т.е. задать значение true для поля IsDeleted
     *
     * @param id - идентификатор задачи
     */
    @Override
    public void delete(Long id) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new UserTaskNotFoundException(id));
        userTask.setDeleted(true);
        userTaskRepository.save(userTask);
    }

    @Override
    public void complete(Long id) {
        UserTask userTask = userTaskRepository.findById(id)
                .orElseThrow(() -> new UserTaskNotFoundException(id));
        userTask.setStatus(Status.COMPLETED);
        userTaskRepository.save(userTask);
    }
}
