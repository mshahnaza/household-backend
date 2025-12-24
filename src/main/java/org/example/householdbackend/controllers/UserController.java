package org.example.householdbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.example.householdbackend.dto.request.UserResponse;
import org.example.householdbackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/all")
    public List<UserResponse> getAllusers() {
        return userService.getAllUsers();
    }
    @GetMapping("/user/{username}")
    public UserResponse getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
    @DeleteMapping("delete/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted user with id " + id);
    }
}
