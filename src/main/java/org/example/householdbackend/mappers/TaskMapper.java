package org.example.householdbackend.mappers;

import org.example.householdbackend.dto.response.TaskResponse;
import org.example.householdbackend.entities.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {
    TaskResponse taskToTaskDto(Task task);

    List<TaskResponse> taskToTaskDtos(List<Task> tasks);

}
