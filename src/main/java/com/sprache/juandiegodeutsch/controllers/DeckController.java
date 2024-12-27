package com.sprache.juandiegodeutsch.controllers;


import com.sprache.juandiegodeutsch.dtos.DeckRequestDTO;
import com.sprache.juandiegodeutsch.dtos.GetDecksResponseDTO;
import com.sprache.juandiegodeutsch.models.Deck;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.services.DeckService;
import com.sprache.juandiegodeutsch.services.FlashcardService;
import com.sprache.juandiegodeutsch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deck")
@RequiredArgsConstructor
public class DeckController {


    private final UserService userService;
    private final DeckService deckService;
    private final FlashcardService flashcardService;



//DECKS


    @GetMapping("/getall")
    public ResponseEntity<List<GetDecksResponseDTO>> getDecks(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(deckService.getUserDecks(user));
    }



    @PostMapping("/create")
    public ResponseEntity<?> createDeck(@Valid @RequestBody DeckRequestDTO request, Principal principal){

        String username= principal.getName();
        User user= userService.findUserByUsername(username);
        Deck createdDeck = null;
        try {
            createdDeck = deckService.createDeck(request, user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Deck successfully created");
        response.put("id", createdDeck.getId());
        response.put("name", createdDeck.getName());
        response.put("description", createdDeck.getDescription());

        return ResponseEntity.ok(response);
    }






    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDeck(@PathVariable Long id, Principal principal) {
        String username= principal.getName();

        User user= userService.findUserByUsername(username);
        deckService.deleteDeck(id, user);
        return ResponseEntity.ok("Deck deleted");
    }



    //COPY DECK FROM A TEMPLATE

    @PostMapping("copy/{templateId}")
    public ResponseEntity<String> copyTemplateToUser(@PathVariable Long templateId, Principal principal) {
        String username = principal.getName();
        try {
            flashcardService.copyTemplateToUser(templateId, username);
            return ResponseEntity.ok("Deck copied successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error copying deck: " + e.getMessage());
        }
    }

}
