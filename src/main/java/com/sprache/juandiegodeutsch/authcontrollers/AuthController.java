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
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request){
        return ResponseEntity.ok(authService.register(request));
    }





    //Route to validate the token in the frontend
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        // Verifing token format
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);  // Token no proporcionado correctamente
        }


        String token = authorizationHeader.substring(7);  // Elimina el prefijo "Bearer "

        // Get username from token
        String username = jwtService.getUsernameFromToken(token);

        // Search username in DB
        UserDetails user = userRepository.findByUsername(username).orElse(null);


        if (user != null && jwtService.isTokenValid(token, user)) {
            return ResponseEntity.ok(true);  // VALID TOKEN
        } else {
            return ResponseEntity.ok(false); // INVALID TOKEN
        }
    }

}

