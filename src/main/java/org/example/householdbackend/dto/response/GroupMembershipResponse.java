package org.example.householdbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.householdbackend.entities.Group;
import org.example.householdbackend.entities.User;
import org.example.householdbackend.enums.Group_Role;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupMembershipResponse {
    private Long id;
    private Long groupId;
    private Long userId;
    private String role;
}
