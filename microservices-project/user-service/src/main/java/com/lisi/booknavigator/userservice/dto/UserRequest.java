package com.lisi.booknavigator.userservice.dto;

import com.lisi.booknavigator.userservice.model.UserType;
import com.lisi.booknavigator.userservice.model.User_profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String username;
    private String email;
    private String passwordhash;
    private UserType role;
    private User_profile profile;
    private String languagereference;
}
