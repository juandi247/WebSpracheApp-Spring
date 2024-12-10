package com.sprache.juandiegodeutsch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "the username can not be username")
    String username;

    @NotBlank(message = "the password can not be empty")
    String password;

    @NotBlank(message = "the email cannot is empty")
    String email;



}
