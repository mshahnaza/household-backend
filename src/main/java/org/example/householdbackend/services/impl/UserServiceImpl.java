package org.example.householdbackend.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.request.UserResponse;
import org.example.householdbackend.entities.User;
import org.example.householdbackend.mappers.UserMapper;
import org.example.householdbackend.repositories.UserRepository;
import org.example.householdbackend.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.userToUserDtos(userRepository.findAll());
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        return userMapper.userToUserDto(userRepository.findUserByUsername(username));
    }

    @Override
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}
