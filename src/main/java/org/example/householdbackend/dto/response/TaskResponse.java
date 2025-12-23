package org.example.householdbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.householdbackend.entities.User;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {
    private int id;
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
    private String frequency;
    private User assignedTo;
}
