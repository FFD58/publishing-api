package ru.fafurin.publishing.mapper;

import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.dto.response.UserTaskAllInfoResponse;
import ru.fafurin.publishing.dto.response.UserTaskResponse;
import ru.fafurin.publishing.entity.UserTask;
import ru.fafurin.publishing.util.DateTimeUtil;

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
                .username(userTask.getUser().getUsername())
                .orderNumber(userTask.getOrder().getNumber())
                .createdAt(DateTimeUtil.toTimestamp(userTask.getCreatedAt()))
                .updatedAt(DateTimeUtil.toTimestamp(userTask.getUpdatedAt()))
                .finishedAt(DateTimeUtil.toTimestamp(userTask.getFinishedAt()))
                .build();
    }

    public static UserTaskAllInfoResponse getUserTaskAllInfoResponse(UserTask userTask) {
        return UserTaskAllInfoResponse.builder()
                .title(userTask.getTitle())
                .status(userTask.getStatus().getTitle())
                .comment(userTask.getComment())
                .createdAt(DateTimeUtil.toTimestamp(userTask.getCreatedAt()))
                .updatedAt(DateTimeUtil.toTimestamp(userTask.getUpdatedAt()))
                .finishedAt(DateTimeUtil.toTimestamp(userTask.getFinishedAt()))
                .order(OrderMapper.getOrderResponse(userTask.getOrder()))
                .user(UserMapper.getUserResponse(userTask.getUser()))
                .build();
    }

}
