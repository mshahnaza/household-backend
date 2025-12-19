package org.example.householdbackend.services;

import org.example.householdbackend.dto.request.GroupRequest;
import org.example.householdbackend.dto.response.GroupResponse;

import java.util.List;

public interface GroupService {
    GroupResponse createGroup(GroupRequest group);
    GroupResponse changeGroupName(Long id, String name);
    GroupResponse changeGroupDescription(Long id, String description);
    void deleteGroup(Long id);
    GroupResponse getGroupInfo(Long id);
    List<GroupResponse> getGroupsByUserId(Long userId);
    GroupResponse createInvintationCode(Long groupId);
}
