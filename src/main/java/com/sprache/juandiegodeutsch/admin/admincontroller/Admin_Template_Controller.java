package com.sprache.juandiegodeutsch.admin.admincontroller;


import com.sprache.juandiegodeutsch.admin.adminservices.Admin_Minigame_wordService;
import com.sprache.juandiegodeutsch.admin.adminservices.Admin_TemplateService;
import com.sprache.juandiegodeutsch.admin.adminservices.Admin_UserService;
import com.sprache.juandiegodeutsch.dtos.*;
import com.sprache.juandiegodeutsch.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class Admin_Template_Controller {
    private final Admin_Minigame_wordService adminMinigameWordService;
    private final Admin_UserService adminUserService;
    private final Admin_TemplateService adminTemplateService;




    //TEMLPATES



    @GetMapping("/templates/getall")
    public ResponseEntity<List<GetTemplateResponseDTO>> getTemplates() {
        return ResponseEntity.ok(adminTemplateService.getTemplates());
    }





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





    @GetMapping("/getByTemplate/{templateID}")
    public ResponseEntity<List<FlashcardResponseDTO>> getFlashcardsByTemplate(@PathVariable Long templateID) {

        List<FlashcardResponseDTO> flashcards = adminTemplateService.getFlaschardByTemplate(templateID);

        return ResponseEntity.ok(flashcards);
    }

}
