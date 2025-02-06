package com.waqas.social.media.platform.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotNull(message = "User name can't not be null")
    @NotEmpty(message = "User name can't be empty")
    private String userName;

    @Email(message = "email format is not correct")
    @NotNull(message = "email can't not be null")
    @NotEmpty(message = "email can't be empty")
    private String email;

    @NotNull(message = "password can't not be null")
    @NotEmpty(message = "password can't be empty")
    private String password;

    private String profilePicture;

    private String bio;

}
