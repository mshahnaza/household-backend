package org.example.householdbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupRequest {
    @NotNull(message = "Group name cannot be null")
    @NotBlank(message = "Group cannot be blank")
    @Size(min = 1, max = 20, message = "Name size cannot exceed 20 characters")
    private String name;

    @Size(max = 100, message = "Description size cannot exceed 100 characters")
    private String description;


}
