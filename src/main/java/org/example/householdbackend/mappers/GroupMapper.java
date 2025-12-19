package org.example.householdbackend.mappers;
import org.example.householdbackend.dto.response.GroupResponse;
import org.example.householdbackend.entities.Group;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {
    List<GroupResponse> groupToGroupDtos(List<Group> groups);
    GroupResponse groupToGroupDto(Group group);
}
