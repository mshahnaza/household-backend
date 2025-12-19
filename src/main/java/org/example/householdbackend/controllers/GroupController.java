package org.example.householdbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.request.GroupRequest;
import org.example.householdbackend.dto.response.GroupResponse;
import org.example.householdbackend.services.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<GroupResponse> createGroup(
            @Valid @RequestBody GroupRequest groupRequest) {
        GroupResponse response = groupService.createGroup(groupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/rename/{groupId}")
    public ResponseEntity<GroupResponse> updateGroupName(@PathVariable Long groupId, @RequestParam String groupName) {
        GroupResponse groupResponse = groupService.changeGroupName(groupId, groupName);
        return ResponseEntity.ok(groupResponse);
    }

    @PatchMapping("/description/{groupId}")
    public ResponseEntity<GroupResponse> updateGroupDescription(@PathVariable Long groupId, @RequestParam String description) {
        GroupResponse groupResponse = groupService.changeGroupDescription(groupId, description);
        return ResponseEntity.ok(groupResponse);
    }

    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info/{groupId}")
    public GroupResponse getGroupInfo(@PathVariable Long groupId) {
        return groupService.getGroupInfo(groupId);
    }

    @GetMapping("/user/{userId}")
    List<GroupResponse> getUsersGroups(@PathVariable Long userId) {
        return groupService.getGroupsByUserId(userId);
    }

    @PatchMapping("/invitation-code/{groupId}")
    GroupResponse generateInvitationCode(@PathVariable Long groupId) {
        return groupService.createInvintationCode(groupId);
    }

    @GetMapping("/members/count/{groupId}")
    public int countMembers(@PathVariable Long groupId) {
        return groupService.countMembers(groupId);
    }
}
