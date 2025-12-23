package org.example.householdbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.request.TaskRequest;
import org.example.householdbackend.dto.response.TaskResponse;
import org.example.householdbackend.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity<TaskResponse> addTask(@RequestBody TaskRequest taskRequest) {
        TaskResponse response = taskService.addTask(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted");
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updaTask(@PathVariable long id, @RequestBody TaskRequest taskRequest) {
        TaskResponse response = taskService.updateTask(id, taskRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse response = taskService.getTaskById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> responses = taskService.getAllTasks();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
