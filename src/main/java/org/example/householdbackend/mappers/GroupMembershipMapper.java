package org.example.householdbackend.mappers;

import org.example.householdbackend.dto.response.GroupMembershipResponse;
import org.example.householdbackend.entities.GroupMembership;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface GroupMembershipMapper {
    GroupMembershipResponse groupMembershipToGroupMembershipDto(GroupMembership groupMembership);
    List<GroupMembershipResponse> groupMembershipToGroupMembershipDtos(List<GroupMembership> groupMemberships);
}
