package com.sprache.juandiegodeutsch.authcontrollers;

import com.sprache.juandiegodeutsch.dtos.AuthResponseDTO;
import com.sprache.juandiegodeutsch.dtos.LoginRequestDTO;
import com.sprache.juandiegodeutsch.dtos.RegisterRequestDTO;
import com.sprache.juandiegodeutsch.repositories.UserRepository;
import com.sprache.juandiegodeutsch.services.AuthService;
import com.sprache.juandiegodeutsch.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;




    //Login unprotected route
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authService.login(request));
    }



    //Register unprotected route
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            AuthResponseDTO response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



/*
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);

        try {
            String username = jwtService.getUsernameFromToken(token);

            UserDetails user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (jwtService.isTokenValid(token, user)) {
                return ResponseEntity.ok(true); // Token válido
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token."); // Token inválido
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }
*/

}

