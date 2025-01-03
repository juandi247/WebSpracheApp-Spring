package com.sprache.juandiegodeutsch.controllers;


import com.sprache.juandiegodeutsch.dtos.EditFlashcardRequestDTO;
import com.sprache.juandiegodeutsch.dtos.FlashcardRequestDTO;
import com.sprache.juandiegodeutsch.dtos.FlashcardResponseDTO;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.services.FlashcardService;
import com.sprache.juandiegodeutsch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flashcard")
@RequiredArgsConstructor
public class FlashcardController {

private final UserService userService;
private final FlashcardService flashcardService;


//FLASHCARDS


    @PostMapping("/create")
    public ResponseEntity<?> createFlashcard(@Valid @RequestBody FlashcardRequestDTO request, Principal principal){

        String username = principal.getName();
        User user=userService.findUserByUsername(username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Flashchards created successfully");

        try {
            flashcardService.createFlashcard(request,user);
        return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @GetMapping("/getByDeck/{deckId}")
    public ResponseEntity<List<FlashcardResponseDTO>> getFlashcardsByDeck(
            @PathVariable Long deckId,
            @AuthenticationPrincipal User user) {

        List<FlashcardResponseDTO> flashcards = flashcardService.getFlashcardsByDeck(deckId, user);

        return ResponseEntity.ok(flashcards);
    }





    @PatchMapping("edit/{id}")
    public ResponseEntity<?> edtiFlaschard(@PathVariable Long id, @RequestBody EditFlashcardRequestDTO requestDTO, Principal principal) {
        String username = principal.getName();
        flashcardService.editFlashcard(id,requestDTO, username);
        return ResponseEntity.ok("Flashcard edited");
    }




    @DeleteMapping("delete/{id}")

    public ResponseEntity<?> deleteFlashcard(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        flashcardService.deleteFlashcard(id, username);
        return ResponseEntity.ok("Flashcard deleteed");
    }




}
