package com.lisi.booknavigator.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User_profile {
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String address;
    private String postcode ;
    private Long imagefieldid;
}

