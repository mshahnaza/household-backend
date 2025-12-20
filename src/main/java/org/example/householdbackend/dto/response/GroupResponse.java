package org.example.householdbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupResponse {
    private Long groupId;
    private String name;
    private String description;
    private String invitationCode;
    private LocalDateTime inviteCodeExpirationTime;
}
