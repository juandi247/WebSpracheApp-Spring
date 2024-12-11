package com.sprache.juandiegodeutsch.admin.admincontroller;


import com.sprache.juandiegodeutsch.admin.adminservices.Admin_Minigame_wordService;
import com.sprache.juandiegodeutsch.admin.adminservices.Admin_TemplateService;
import com.sprache.juandiegodeutsch.admin.adminservices.Admin_UserService;
import com.sprache.juandiegodeutsch.dtos.Minigame_Word_RequestDTO;
import com.sprache.juandiegodeutsch.dtos.Minigame_Word_ResponseDTO;
import com.sprache.juandiegodeutsch.dtos.TemplateRequestDTO;
import com.sprache.juandiegodeutsch.dtos.Template_FlashcardRequestDTO;
import com.sprache.juandiegodeutsch.models.Minigame_word;
import com.sprache.juandiegodeutsch.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdministratorController {

//Dependencies inyections (services)
    private final Admin_Minigame_wordService adminMinigameWordService;
    private final Admin_UserService adminUserService;
    private final Admin_TemplateService adminTemplateService;

//USERS

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = "0") int page)
    {
        int fixedPageSize = 10;  // Tamaño fijo para todas las páginas
        Page<User> users = adminUserService.getAllUsers(page, fixedPageSize);
        return ResponseEntity.ok(users);
    }


//minigame words

@GetMapping("/minigame/get_words")
public ResponseEntity<Minigame_Word_ResponseDTO> getWordsByCategory(@RequestParam String category ){

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








    //TEMLPATES


    @PostMapping("/templates/create")
    public ResponseEntity<?> createTemplate(@Valid @RequestBody TemplateRequestDTO request) {
        try {
            adminTemplateService.createTemplate(request);

            return ResponseEntity.ok("Template created succesfully");
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }

    }




    @DeleteMapping("/templates/delete/{id}")
    public ResponseEntity<?> deleteTemplate(@PathVariable Long id){
        try {
            adminTemplateService.deleteTemplate(id);
            return ResponseEntity.ok("Template with ID " + id + " was successfully deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }






    //FLASHCARDSSS


    @PostMapping("/flashcard/create")
    public ResponseEntity<?> createTemplateFlashcards(@Valid @RequestBody Template_FlashcardRequestDTO request){

        try {
            adminTemplateService.createTemplateFlashcards(request);
            return ResponseEntity.ok("Falschards created succesfully");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @DeleteMapping("/flashcard/delete/{id}")
    public ResponseEntity<?> deleteTemplateFlashcard(@PathVariable Long id){
        try {
            adminTemplateService.deleteFlashcard(id);
            return ResponseEntity.ok("Flashcard with ID " + id + " was successfully deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}