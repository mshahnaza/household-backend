package org.example.householdbackend.services.impl;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.request.TaskRequest;
import org.example.householdbackend.dto.response.TaskResponse;
import org.example.householdbackend.entities.Task;
import org.example.householdbackend.mappers.TaskMapper;
import org.example.householdbackend.repositories.TaskRepository;
import org.example.householdbackend.services.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse addTask(TaskRequest taskRequest) {
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(null)
                .dueDate(taskRequest.getDueDate())
                .completedAt(null)
                .frequency(taskRequest.getFrequency())
                .assignedTo(taskRequest.getAssignedTo())
                .group(null)
                .createdBy(null)
                .build();

        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskResponse updateTask(long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        return taskMapper.taskToTaskDto(task);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.taskToTaskDtos(tasks);
    }
}
