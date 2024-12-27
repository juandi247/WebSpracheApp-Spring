package com.sprache.juandiegodeutsch.controllers;


import com.sprache.juandiegodeutsch.dtos.AIDeckCreationRequestDTO;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.services.AIService;
import com.sprache.juandiegodeutsch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {


    private final AIService aiService;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<List<Map<String, String>>> createDeckwithAI(@RequestBody AIDeckCreationRequestDTO requestDTO, Principal principal) {
        String username= principal.getName();

        User user= userService.findUserByUsername(username);

        List<Map<String, String>> flashcards = aiService.generateDeckWithAI(user, requestDTO);

        return ResponseEntity.ok(flashcards);
    }
}
