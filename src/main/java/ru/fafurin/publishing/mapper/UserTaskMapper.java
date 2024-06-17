package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.model.UserTask;

public class UserTaskMapper {

    public static UserTask getUserTask(UserTask userTask, UserTaskRequest userTaskRequest) {
        userTask.setTitle(userTaskRequest.getTitle());
        return userTask;
    }

}
