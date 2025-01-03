package com.sprache.juandiegodeutsch.admin.admincontroller;


import com.sprache.juandiegodeutsch.admin.adminservices.Admin_Minigame_wordService;
import com.sprache.juandiegodeutsch.admin.adminservices.Admin_TemplateService;
import com.sprache.juandiegodeutsch.admin.adminservices.Admin_UserService;
import com.sprache.juandiegodeutsch.dtos.Minigame_Word_RequestDTO;
import com.sprache.juandiegodeutsch.dtos.Minigame_Word_ResponseDTO;
import com.sprache.juandiegodeutsch.models.Minigame_word;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class Admin_Minigame_controller {

    private final Admin_Minigame_wordService adminMinigameWordService;
    private final Admin_UserService adminUserService;
    private final Admin_TemplateService adminTemplateService;



    @GetMapping("/minigame/get_words/{category}")
    public ResponseEntity<Minigame_Word_ResponseDTO> getWordsByCategory(@PathVariable String category ){

        Minigame_Word_ResponseDTO response=adminMinigameWordService.getwordsByCategory(category);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/minigame/add_words")
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




    @DeleteMapping("/minigame/deleteword/{id}")
    public ResponseEntity<?> deleteWord(@PathVariable Long id){
        try {
            adminMinigameWordService.deleteWord(id);
            return ResponseEntity.ok("Word with ID " + id + " was successfully deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}
