package com.lisi.booknavigator.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(value = "User")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String passwordhash;
    private UserType role;
    private User_profile profile;
    private String languagereference;

}


