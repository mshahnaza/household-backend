package org.example.householdbackend.mappers;

import org.example.householdbackend.dto.request.UserResponse;
import org.example.householdbackend.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    List<UserResponse> userToUserDtos(List<User> all);

    UserResponse userToUserDto(Usergi userByUsername);
}
