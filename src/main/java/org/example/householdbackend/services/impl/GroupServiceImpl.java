package org.example.householdbackend.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.request.GroupRequest;
import org.example.householdbackend.dto.response.GroupResponse;
import org.example.householdbackend.entities.Group;
import org.example.householdbackend.entities.GroupMembership;
import org.example.householdbackend.enums.Group_Role;
import org.example.householdbackend.mappers.GroupMapper;
import org.example.householdbackend.repositories.GroupMembershipRepository;
import org.example.householdbackend.repositories.GroupRepository;
import org.example.householdbackend.services.GroupService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GroupMembershipRepository groupMembershipRepository;

    @Override
    public GroupResponse createGroup(GroupRequest groupRequest) {
        if (groupRequest == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }

        //User currentUser = userService.getCurrentUser();

        Group group = Group.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
//                .createdBy(currentUser);
                .build();

        groupRepository.save(group);

        GroupMembership groupMembership = GroupMembership.builder()
                .group(group)
//                .user(currentUser)
                .role(Group_Role.ROLE_ADMIN)
                .build();
        groupMembershipRepository.save(groupMembership);

        return groupMapper.groupToGroupDto(group);
    }

    @Override
    public GroupResponse changeGroupName(Long id, String name) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " not found"));

        group.setName(name);
        groupRepository.save(group);
        return groupMapper.groupToGroupDto(group);
    }

    @Override
    public GroupResponse changeGroupDescription(Long id, String description) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " not found"));

        group.setDescription(description);
        groupRepository.save(group);
        return groupMapper.groupToGroupDto(group);
    }

    @Override
    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " not found"));
        groupRepository.delete(group);
    }

    @Override
    public GroupResponse getGroupInfo(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + id + " not found"));
        return groupMapper.groupToGroupDto(group);
    }

    @Override
    public List<GroupResponse> getGroupsByUserId(Long userId) {
        List<Group> groups = groupRepository.findByUserId(userId);
        return groupMapper.groupToGroupDtos(groups);
    }

    @Override
    public GroupResponse createInvintationCode(Long groupId) {
        String invitationCode = generateRandomCode();
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + groupId + " not found"));
        group.setInvitationCode(invitationCode);
        group.setInviteCodeExpirationTime(LocalDateTime.now().plusHours(1));
        groupRepository.save(group);
        return groupMapper.groupToGroupDto(group);
    }

    @Override
    public int countMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group with id " + groupId + " not found"));

        return group.getMembers().size();
    }

    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }
}
