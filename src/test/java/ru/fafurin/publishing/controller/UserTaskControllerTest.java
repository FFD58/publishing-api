package ru.fafurin.publishing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.fafurin.publishing.dto.request.UserTaskRequest;
import ru.fafurin.publishing.exception.UserTaskNotFoundException;
import ru.fafurin.publishing.entity.Order;
import ru.fafurin.publishing.entity.User;
import ru.fafurin.publishing.entity.UserTask;
import ru.fafurin.publishing.service.UserTaskService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserTaskControllerTest {

    private static final String END_POINT_PATH = "/api/v1/users/tasks";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserTaskService service;
    private UserTask userTask;
    private UserTaskRequest userTaskRequest;
    private String requestURI;
    private Long userTaskId;

    @BeforeEach
    public void init() {
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
        userTaskId = 111L;
        requestURI = END_POINT_PATH + "/" + userTaskId;
    }

    /**
     * Проверить статус Created при успешном сохранении новой задачи сотрудника
     */
    @Test
    public void Save_Returns201Created() throws Exception {
        when(service.save(userTaskRequest)).thenReturn(userTask);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(objectMapper.writeValueAsString(userTaskRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userTask)))
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest в случае, если запрос не прошел валидацию
     */
    @Test
    public void SaveThenInvalidRequest_Returns400BadRequest() throws Exception {
        UserTaskRequest invalidUserTaskRequest = UserTaskRequest.builder()
                .title(null)
                .build();

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUserTaskRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить статус NotFound в случае, если задача сотрудника не найдена по идентификатору
     */
    @Test
    public void GetByIdThenWrongId_Returns404NotFound() throws Exception {
        when(service.get(userTaskId)).thenThrow(UserTaskNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное получение задачи сотрудника по идентификатору
     */
    @Test
    public void GetById_Returns200OK() throws Exception {
        UserTask userTask = Mockito.mock(UserTask.class);

        when(service.get(userTaskId)).thenReturn(userTask);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(userTask)))
                .andDo(print());
    }

    /**
     * Проверить статус NoContent в случае, если вернется пустой список задач
     */
    @Test
    public void GetAllThenEmptyList_Returns204NoContent() throws Exception {
        when(service.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Проверить успешное получение списка всех задач
     */
    @Test
    public void GetAll_Returns200OK() throws Exception {
        List<UserTask> userTasks = List.of(
                Mockito.mock(UserTask.class),
                Mockito.mock(UserTask.class),
                Mockito.mock(UserTask.class)
        );

        when(service.getAll()).thenReturn(userTasks);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(userTasks)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при изменении задачи сотрудника, когда задача не найдена по идентификатору
     */
    @Test
    public void UpdateThenWrongId_Returns404NotFound() throws Exception {
        when(service.update(userTaskId, userTaskRequest))
                .thenThrow(UserTaskNotFoundException.class);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userTaskRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить статус BadRequest при изменении задачи сотрудника, если запрос не прошел валидацию
     */
    @Test
    public void UpdateThenInvalidRequest_Returns400BadRequest() throws Exception {
        UserTaskRequest invalidUserTaskRequest = UserTaskRequest.builder()
                .title(null)
                .build();

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUserTaskRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * Проверить успешное изменение задачи сотрудника
     */
    @Test
    public void Update_Returns200OK() throws Exception {
        when(service.update(userTaskId, userTaskRequest)).thenReturn(userTask);

        mockMvc.perform(put(requestURI)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userTaskRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userTask)))
                .andDo(print());
    }

    /**
     * Проверить статус NotFound при удалении задачи сотрудника, когда задача не найдена по идентификатору
     */
    @Test
    public void DeleteThenWrongId_Returns404NotFound() throws Exception {
        Mockito.doThrow(UserTaskNotFoundException.class).when(service).delete(userTaskId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * Проверить успешное удаление задачи сотрудника
     */
    @Test
    public void Delete_Returns200OK() throws Exception {
        Mockito.doNothing().when(service).delete(userTaskId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("User task with id: %d deleted", userTaskId)))
                .andDo(print());
    }

}
