package com.sprache.juandiegodeutsch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "the username can not be empty")
    String username;

    @NotBlank(message = "the password can not be empty")
    @Size(min = 5, message = "The password must be at least 5 characters long")
    String password;

    @NotBlank(message = "the email cannot is empty")
    String email;



}
