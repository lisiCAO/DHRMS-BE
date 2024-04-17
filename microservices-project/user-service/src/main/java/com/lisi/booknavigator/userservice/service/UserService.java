package com.lisi.booknavigator.userservice.service;

import com.lisi.booknavigator.userservice.dto.UserRequest;
import com.lisi.booknavigator.userservice.dto.UserResponse;
import com.lisi.booknavigator.userservice.event.UserEvent;
import com.lisi.booknavigator.userservice.model.User;
import com.lisi.booknavigator.userservice.model.User_profile;
import com.lisi.booknavigator.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void createUser(UserRequest userRequest){

        User user = User.builder()

                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .passwordhash(userRequest.getPasswordhash())
                .role(userRequest.getRole())
                .profile(new User_profile(
                        userRequest.getProfile().getFirstName(),
                        userRequest.getProfile().getLastName(),
                        userRequest.getProfile().getPhoneNo(),
                        userRequest.getProfile().getAddress(),
                        userRequest.getProfile().getPostcode(),
                        userRequest.getProfile().getImagefieldid()
                        ))
                .languagereference(userRequest.getLanguagereference())
                .build();

        User savedUser = userRepository.save(user);

        UserEvent event = new UserEvent(String.valueOf(savedUser.getId()), UserEvent.EventType.CREATE, savedUser);
        kafkaTemplate.send("UserTopic",event);

        log.info("User {} is saved", user.getId());
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();

        log.info("Retrieved {} Users", users.size());

        return users.stream().map(this::mapToUserResponse).toList();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .passwordhash(user.getPasswordhash())
                .role(user.getRole())
                .profile(user.getProfile())
                .languagereference(user.getLanguagereference())
                .build();

    }

    public UserResponse getUserById(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            log.info("User {} retrieved", user);
            return mapToUserResponse(user);
        } else {
            log.info("User ID {} not found.", userId);
            return null;
        }
    }

    public UserResponse updateUserById(String historyId, UserRequest userRequest) {
        Optional<User> userOpt = userRepository.findById(historyId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            //update User fields
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPasswordhash(userRequest.getPasswordhash());
            user.setRole(userRequest.getRole());
            user.setProfile(new User_profile(
                    userRequest.getProfile().getFirstName(),
                    userRequest.getProfile().getLastName(),
                    userRequest.getProfile().getPhoneNo(),
                    userRequest.getProfile().getAddress(),
                    userRequest.getProfile().getPostcode(),
                    userRequest.getProfile().getImagefieldid()));
            user.setLanguagereference(userRequest.getLanguagereference());


            // save the updated User
            User updatedUser = userRepository.save(user);

            // convert the updated User to response object
            log.info("User {} updated", user);
            return mapToUserResponse(updatedUser);
        } else {
            log.info("User ID {} not found.", historyId);
            return null;
        }
    }

    public boolean deleteUserById(String historyId) {
        if (userRepository.existsById(historyId)) {
            userRepository.deleteById(historyId);
            log.info("User ID {} deleted", historyId);
            return true; // delete operation executed successfully
        } else {
            log.info("User ID {} not found.", historyId);

            return false; // User not found
        }
    }
}