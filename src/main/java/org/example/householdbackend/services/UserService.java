package org.example.householdbackend.services;

import org.example.householdbackend.dto.request.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserByUsername(String username);

    void deleteUser(long id);
}
