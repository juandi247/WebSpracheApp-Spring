package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.dtos.DeckRequestDTO;
import com.sprache.juandiegodeutsch.dtos.FlashcardResponseDTO;
import com.sprache.juandiegodeutsch.dtos.GetTemplateResponseDTO;
import com.sprache.juandiegodeutsch.dtos.Template_FlashcardRequestDTO;
import com.sprache.juandiegodeutsch.models.*;
import com.sprache.juandiegodeutsch.repositories.TemplateRepository;
import com.sprache.juandiegodeutsch.repositories.Template_FlashcardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateService {


    private final TemplateRepository templateRepository;
    private final Template_FlashcardRepository templateFlashcardRepository;


    @Cacheable(value = "templates", key = "'allTemplates'")
    public List<GetTemplateResponseDTO> getTemplates() {

        List<Template> templates = templateRepository.findAll();

        return templates.stream()
                .map(template -> new GetTemplateResponseDTO(
                        template.getId(),
                        template.getName(),
                        template.getDescription(),
                        template.getLenguageLevel().name(),
                        template.getTotalWords()
                ))
                .collect(Collectors.toList());
    }







    //  @Cacheable(value = "flashcards", key = "#deckId + '-' + #username")
    public List<FlashcardResponseDTO> getFlaschardByTemplate(Long templateID) {

        Template template = templateRepository.findById(templateID)
                .orElseThrow(() -> new RuntimeException("Deck not found"));


        List<Template_flashcard> flashcards = templateFlashcardRepository.findByTemplate(template);


        return flashcards.stream()
                .map(flashcard -> new FlashcardResponseDTO(
                        flashcard.getId(),
                        flashcard.getFront(),
                        flashcard.getReverse(),
                        flashcard.getAudio()))
                .collect(Collectors.toList());
    }








}
