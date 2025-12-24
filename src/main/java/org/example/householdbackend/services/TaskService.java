package org.example.householdbackend.services;

import org.example.householdbackend.dto.request.TaskRequest;
import org.example.householdbackend.dto.request.TaskStatusRequest;
import org.example.householdbackend.dto.response.TaskResponse;
import org.example.householdbackend.dto.response.TaskStatusResponse;

import java.util.List;

public interface TaskService {

    TaskResponse addTask(TaskRequest taskRequest);

    void deleteTask(Long id);

    TaskResponse updateTask(long id, TaskRequest taskRequest);

    TaskResponse getTaskById(Long id);

    List<TaskResponse> getAllTasks();

    TaskStatusResponse updateTaskStatus(long id, TaskStatusRequest taskStatusRequest);
}
