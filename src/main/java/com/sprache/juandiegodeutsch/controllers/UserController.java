package com.sprache.juandiegodeutsch.controllers;


import com.sprache.juandiegodeutsch.repositories.UserRepository;
import com.sprache.juandiegodeutsch.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class UserController {


    @GetMapping("/user")
    public ResponseEntity<?> validateToken(Principal principal) {
        if (principal != null) {
            Map<String, String> response = new HashMap<>();
            response.put("username", principal.getName());
            response.put("status", "Token is valid");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
    }

}
