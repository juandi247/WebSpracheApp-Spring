package com.sprache.juandiegodeutsch.dtos;


import com.sprache.juandiegodeutsch.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminGetUsersResponseDTO {
     Long id;
     String username;
     String email;
     LocalDateTime creationDate;
     Role role;
}
