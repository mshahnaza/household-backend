package org.example.householdbackend.services;

import org.example.householdbackend.dto.response.GroupMembershipResponse;
import org.example.householdbackend.enums.Group_Role;

import java.util.List;

public interface GroupMembershipService {
    GroupMembershipResponse joinGroupByCode(String code);
    void removeMember(Long memberId);
    GroupMembershipResponse changeMemberRole(Long groupId, Long memberId, String role);
    List<GroupMembershipResponse> getGroupMembers(Long groupId);
}
