package com.sprache.juandiegodeutsch.controllers;


import com.sprache.juandiegodeutsch.dtos.FlashcardResponseDTO;
import com.sprache.juandiegodeutsch.dtos.ProgressRequestDTO;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.services.ProgressService;
import com.sprache.juandiegodeutsch.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/progress")
public class ProgressController {



    private final UserService userService;
    private final ProgressService progressService;




    @GetMapping("/flashcards/{deckId}")
    public ResponseEntity<List<FlashcardResponseDTO>> getFlashcardsToReview(
            @PathVariable Long deckId,
            @AuthenticationPrincipal User user) {

        List<FlashcardResponseDTO> flashcards = progressService.getFlashcardsToReviewByDeck(deckId, user.getUsername());
        return ResponseEntity.ok(flashcards);
    }






    @PatchMapping
    public ResponseEntity<?> reviewFlashcards(@Valid @RequestBody ProgressRequestDTO requestDTO, Principal principal){


        try {
            String username = principal.getName();
            User user = userService.findUserByUsername(username);

            progressService.reviewProgress(requestDTO, user);

            return ResponseEntity.ok("Flashcard progress updated successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating flashcard progress: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred.");
        }
    }
}
