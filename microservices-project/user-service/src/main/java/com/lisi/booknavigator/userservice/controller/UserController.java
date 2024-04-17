package com.lisi.booknavigator.userservice.controller;

import com.lisi.booknavigator.userservice.dto.UserRequest;
import com.lisi.booknavigator.userservice.dto.UserResponse;
import com.lisi.booknavigator.userservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/profile")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserRequest userRequest) {
        try {
            userService.createUser(userRequest);
            log.info("User created successfully.");

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
        } catch (Exception e) {
            log.error("Error creating User: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating User: " + e.getMessage());
        }
    }



    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<UserResponse> users = userService.getAllUsers();
            if (users.isEmpty()) {
                log.info("No User found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No User found.");
            } else {
                log.info("Retrieved {} Users", users.size());
                return ResponseEntity.ok(users);
            }
        } catch (Exception e) {
            log.error("Error retrieving Users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving Users: " + e.getMessage());
        }
    }




    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable String userId) {
        try {
            UserResponse user = userService.getUserById(userId);
            if (user != null) {
                log.info("User Id {} retrieved successfully.", userId);
                return ResponseEntity.ok(user);
            } else {
                log.info("User ID {} not found.", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving User: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving User: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUserById(@PathVariable String userId, @RequestBody UserRequest userRequest) {
        try {
            UserResponse updatedUser = userService.updateUserById(userId, userRequest);
            if (updatedUser != null) {
                log.info("User Id {} updated successfully.", userId);
                return ResponseEntity.ok("User updated successfully.");
            } else {
                log.info("User ID {} not found.", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving User: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating User: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        try{
            boolean isDeleted = userService.deleteUserById(userId);
            if (isDeleted) {
                log.info("User Id {} deleted successfully.", userId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                log.info("User ID {} not found.", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            log.error("Error retrieving User: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting User: " + e.getMessage());

        }
    }

}
