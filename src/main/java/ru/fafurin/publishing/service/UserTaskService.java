package ru.fafurin.publishing.service;

import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.dto.response.task.UserTaskAllInfoResponse;
import ru.fafurin.publishing.dto.response.task.UserTaskAddInfoResponse;
import ru.fafurin.publishing.entity.UserTask;

import java.security.Principal;
import java.util.List;

public interface UserTaskService {
    List<UserTaskAddInfoResponse> getAll();

    UserTaskAllInfoResponse get(Long id);

    List<UserTaskAddInfoResponse> getAllByUser(Principal principal);

    UserTask save(UserTaskRequest userTaskRequest);

    UserTask update(Long id, UserTaskRequest userTaskRequest);

    void delete(Long id);

    void complete(Long id);
}
