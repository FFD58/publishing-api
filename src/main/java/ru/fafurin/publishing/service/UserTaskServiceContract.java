package ru.fafurin.publishing.service;

import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.dto.response.UserTaskAllInfoResponse;
import ru.fafurin.publishing.dto.response.UserTaskResponse;
import ru.fafurin.publishing.entity.UserTask;

import java.security.Principal;
import java.util.List;

public interface UserTaskServiceContract {
    List<UserTaskResponse> getAll();

    UserTaskAllInfoResponse get(Long id);

    List<UserTaskResponse> getAllByUser(Principal principal);

    UserTask save(UserTaskRequest userTaskRequest);

    UserTask update(Long id, UserTaskRequest userTaskRequest);

    void delete(Long id);

    void complete(Long id);
}
