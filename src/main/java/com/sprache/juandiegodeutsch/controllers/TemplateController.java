package com.sprache.juandiegodeutsch.controllers;


import com.sprache.juandiegodeutsch.dtos.FlashcardResponseDTO;
import com.sprache.juandiegodeutsch.dtos.GetTemplateResponseDTO;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.services.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/template")
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping("/preview/{templateID}")
    public ResponseEntity<List<FlashcardResponseDTO>> getFlashcardsByTemplate(@PathVariable Long templateID) {

        List<FlashcardResponseDTO> flashcards = templateService.getFlaschardByTemplate(templateID);

        return ResponseEntity.ok(flashcards);
    }



    @GetMapping("/getall")
    public ResponseEntity<List<GetTemplateResponseDTO>> getTemplates(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(templateService.getTemplates());
    }







}
