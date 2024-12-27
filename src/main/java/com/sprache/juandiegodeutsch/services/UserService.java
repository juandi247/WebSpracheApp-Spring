package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    public User findUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }


    public User save(User user) {
        return userRepository.save(user); // Guarda el usuario después de modificarlo
    }
}
