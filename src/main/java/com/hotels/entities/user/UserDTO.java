package com.hotels.entities.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private int hotelId;
}
