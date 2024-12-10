package com.sprache.juandiegodeutsch.admin.admincontroller;


import com.sprache.juandiegodeutsch.admin.adminservices.Admin_Minigame_wordService;
import com.sprache.juandiegodeutsch.dtos.Minigame_Word_RequestDTO;
import com.sprache.juandiegodeutsch.dtos.Minigame_Word_ResponseDTO;
import com.sprache.juandiegodeutsch.models.Minigame_word;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class admincontroller {



    private final Admin_Minigame_wordService adminMinigameWordService;



    @GetMapping("/get_words")
    public ResponseEntity<Minigame_Word_ResponseDTO> getWordsByCategory(@RequestParam String category ){

        Minigame_Word_ResponseDTO response=adminMinigameWordService.getwordsByCategory(category);

        return ResponseEntity.ok(response);



    }









    @PostMapping("/add_words")
    public ResponseEntity<String> addWords(@RequestBody Minigame_Word_RequestDTO request) {
        try {

            List<Minigame_word> addedWords = adminMinigameWordService.createWords(request);

            String message = String.format("%d words added successfully to category %s.", addedWords.size(), request.getCategory());


            return ResponseEntity.ok(message);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add words: " + e.getMessage());
        }
    }


}
