package org.example.householdbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.response.GroupMembershipResponse;
import org.example.householdbackend.services.GroupMembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group/member")
@RequiredArgsConstructor
public class GroupMembershipController {
    private final GroupMembershipService groupMembershipService;

    @PostMapping("/join")
    public ResponseEntity<GroupMembershipResponse> joinGroupByCode(@RequestParam String code) {
        GroupMembershipResponse response = groupMembershipService.joinGroupByCode(code);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/remove/{userId}")
    public ResponseEntity<Void> removeGroupMember(@PathVariable Long userId) {
        groupMembershipService.removeMember(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{groupId}/members/{memberId}/role")
    public ResponseEntity<GroupMembershipResponse> changeMemberRole(@PathVariable Long groupId, @PathVariable Long memberId, @RequestParam String role) {
        GroupMembershipResponse response = groupMembershipService.changeMemberRole(groupId, memberId, role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/{groupId}")
    List<GroupMembershipResponse> getGroupMembers(@PathVariable Long groupId) {
        return groupMembershipService.getGroupMembers(groupId);
    }
}
