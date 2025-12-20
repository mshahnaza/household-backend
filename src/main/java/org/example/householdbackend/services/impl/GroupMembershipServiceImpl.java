package org.example.householdbackend.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.response.GroupMembershipResponse;
import org.example.householdbackend.entities.Group;
import org.example.householdbackend.entities.GroupMembership;
import org.example.householdbackend.entities.User;
import org.example.householdbackend.enums.Group_Role;
import org.example.householdbackend.mappers.GroupMembershipMapper;
import org.example.householdbackend.repositories.GroupMembershipRepository;
import org.example.householdbackend.repositories.GroupRepository;
import org.example.householdbackend.services.GroupMembershipService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMembershipServiceImpl implements GroupMembershipService {
    private final GroupMembershipRepository groupMembershipRepository;
    private final GroupRepository groupRepository;
    private final GroupMembershipMapper groupMembershipMapper;

    @Override
    public GroupMembershipResponse joinGroupByCode(String code) {
        Group group = groupRepository.findByInvitationCode(code)
                .orElseThrow(() -> new RuntimeException("Group not found"));

//        User user = userService.getCurrentUser();

        GroupMembership groupMembership = GroupMembership.builder()
                .group(group)
                .role(Group_Role.ROLE_USER)
//                .user(user)
                .build();
        return null;
    }

    @Override
    public void removeMember(Long memberId) {
//        User user = userRepository.findById(memberId)
//        GroupMembership groupMembership = groupMembershipRepository.findByUser(user);
//        groupMembershipRepository.delete(groupMembership);
    }

    @Override
    public GroupMembershipResponse changeMemberRole(Long groupId, Long memberId, String role) {
//        User currentUser = userService.getCurrentUser();
//        if (currentUser.getId.equals(memberId)) throw new RuntimeException("You cannot change your own role");
        GroupMembership groupMembership = groupMembershipRepository.findByGroup_IdAndUser_Id(groupId, memberId);
        if(role.equals(Group_Role.ROLE_USER.toString())) {
            groupMembership.setRole(Group_Role.ROLE_USER);
        } else {
            groupMembership.setRole(Group_Role.ROLE_ADMIN);
        }
        groupMembershipRepository.save(groupMembership);
        return groupMembershipMapper.groupMembershipToGroupMembershipDto(groupMembership);
    }

    @Override
    public List<GroupMembershipResponse> getGroupMembers(Long groupId) {
        List<GroupMembership> groupMembership = groupMembershipRepository.findAllByGroup_Id(groupId);
        return groupMembershipMapper.groupMembershipToGroupMembershipDtos(groupMembership);
    }
}
