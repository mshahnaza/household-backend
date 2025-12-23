package org.example.householdbackend.dto.request;

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
public class TaskRequest {
    private String title;
    private String description;
    private User assignedTo;
    private LocalDate dueDate;
    private String frequency;
}
