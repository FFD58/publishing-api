package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.dto.response.task.UserTaskAddInfoResponse;
import ru.fafurin.publishing.dto.response.task.UserTaskAllInfoResponse;
import ru.fafurin.publishing.dto.response.task.UserTaskResponse;
import ru.fafurin.publishing.entity.UserTask;

public class UserTaskMapper {

    public static UserTask getUserTask(UserTask userTask, UserTaskRequest userTaskRequest) {
        userTask.setTitle(userTaskRequest.getTitle());
        userTask.setComment(userTaskRequest.getComment());
        return userTask;
    }

    public static UserTaskResponse getUserTaskResponse(UserTask userTask) {
        return UserTaskResponse.builder()
                .id(userTask.getId())
                .title(userTask.getTitle())
                .status(userTask.getStatus().getTitle())
                .comment(userTask.getComment())
                .createdAt(userTask.getCreatedAt())
                .updatedAt(userTask.getUpdatedAt())
                .finishedAt(userTask.getFinishedAt())
                .build();
    }

    public static UserTaskAddInfoResponse getUserTaskAddInfoResponse(UserTask userTask) {
        return UserTaskAddInfoResponse.builder()
                .task(getUserTaskResponse(userTask))
                .username(userTask.getUser().getUsername())
                .orderNumber(userTask.getOrder().getNumber())
                .build();
    }

    public static UserTaskAllInfoResponse getUserTaskAllInfoResponse(UserTask userTask) {
        return UserTaskAllInfoResponse.builder()
                .task(getUserTaskResponse(userTask))
                .order(OrderMapper.getOrderAddInfoResponse(userTask.getOrder()))
                .user(UserMapper.getUserResponse(userTask.getUser()))
                .build();
    }

}
