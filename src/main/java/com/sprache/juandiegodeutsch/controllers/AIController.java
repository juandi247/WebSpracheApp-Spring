package com.sprache.juandiegodeutsch.controllers;


import com.sprache.juandiegodeutsch.dtos.*;
import com.sprache.juandiegodeutsch.models.Deck;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.services.AIService;
import com.sprache.juandiegodeutsch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {


    private final AIService aiService;
    private final UserService userService;


    
//Works for generate a deck but not saves anything, just returns the flaschards but it doesnt create any deck!
    @PostMapping()
    public ResponseEntity<List<Map<String, String>>> generateDeckwithAI(@RequestBody AIDeckCreationRequestDTO requestDTO, Principal principal) {
        String username = principal.getName();

        User user = userService.findUserByUsername(username);

        List<Map<String, String>> flashcards = aiService.generateDeckWithAI(user, requestDTO);

        return ResponseEntity.ok(flashcards);
    }



    @PostMapping("/correct-sentence")
    public ResponseEntity<String> correctSentence(@RequestBody String sentence, Principal principal) {
        String username = principal.getName();

        User user = userService.findUserByUsername(username);

        String correctedSentence = aiService.correctSentenceInGerman(user, sentence);

        return ResponseEntity.ok(correctedSentence);
    }





    @PostMapping("/generate-instructions")
    public ResponseEntity<String> generateInstructions(@RequestBody InstructionsAIRequestDTO requestDTO, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);

        String instructions = aiService.generateInstructions(user, requestDTO);

        return ResponseEntity.ok(instructions);
    }




    @PostMapping("/correct-text")
    public ResponseEntity<String> correctText(@RequestBody CorrectTextAIRequestDTO requestDTO, Principal principal) {
        String username = principal.getName();
        User user = userService.findUserByUsername(username);

        String instructions = aiService.correctText(user, requestDTO);

        return ResponseEntity.ok(instructions);
    }










    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createDeckWithFlashcards(
            @Valid @RequestBody CreateDeckAIRequestDTO request,
            Principal principal) {

        String username = principal.getName();
        User user = userService.findUserByUsername(username);

        Map<String, Object> response = new HashMap<>();

        try {
            Deck createdDeck = aiService.createDeckWithAI(request, user);

            response.put("message", "Deck and flashcards successfully created");
            response.put("id", createdDeck.getId());
            response.put("name", createdDeck.getName());
            response.put("description", createdDeck.getDescription());

            return ResponseEntity.ok(response); // 200 OK

        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid input provided");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request

        } catch (RuntimeException e) {
            response.put("message", "Error creating deck and flashcards. Please try again later.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 Internal Server Error
        }
    }







}