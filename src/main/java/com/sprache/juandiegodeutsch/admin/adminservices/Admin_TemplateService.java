package com.sprache.juandiegodeutsch.admin.adminservices;


import com.sprache.juandiegodeutsch.dtos.*;
import com.sprache.juandiegodeutsch.models.*;
import com.sprache.juandiegodeutsch.repositories.TemplateRepository;
import com.sprache.juandiegodeutsch.repositories.Template_FlashcardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Admin_TemplateService {


    private final TemplateRepository templateRepository;
    private final Template_FlashcardRepository templateFlashcardRepository;




    //Templates
    @CacheEvict(value = "templates", key = "'allTemplates'")
    @Transactional
    public Template createTemplate(TemplateRequestDTO request) {

        if (templateRepository.existsByName(request.getName())) {
            throw new IllegalStateException("Name of the template already exists");
        }

        Template template = new Template();
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setCreation_date(LocalDateTime.now());
        LenguageLevel level = LenguageLevel.valueOf(request.getLenguageLevel().toUpperCase());
        template.setLenguageLevel(level);
        template.setTotalWords(0);

        return templateRepository.save(template);
    }





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





    @CacheEvict(value = "templates", key = "'allTemplates'")
    public void deleteTemplate(Long id_template){
       if(!templateRepository.existsById(id_template)){
           throw new RuntimeException("Template with id "+ id_template + "doesnt exist");

       }
        templateRepository.deleteById(id_template);
    }








    //FLASHCARDS
    @CacheEvict(value = "templates", key = "'allTemplates'")
    @Transactional
    public List<Template_flashcard> createTemplateFlashcards(Template_FlashcardRequestDTO request) {

        if (request.getTemplate_flashcards() == null || request.getTemplate_flashcards().isEmpty()) {
            throw new IllegalArgumentException("The list of flashcards is empty or null");
        }

        Template template = templateRepository.findById(request.getId_template())
                .orElseThrow(() -> new IllegalStateException("Template with id " + request.getId_template() + " does not exist"));

        List<Template_flashcard> newFlashcards = new ArrayList<>();

        request.getTemplate_flashcards().forEach((front, flashcardData) -> {
            Template_flashcard flashcard = new Template_flashcard();

            flashcard.setFront(front);
            flashcard.setReverse(flashcardData.getReverse());
            flashcard.setAudio(flashcardData.getAudio());
            flashcard.setTemplate(template);

            newFlashcards.add(flashcard);
        });


        List<Template_flashcard> savedFlashcards = templateFlashcardRepository.saveAll(newFlashcards);

        int currentTotalWords = template.getTotalWords();
        template.setTotalWords(currentTotalWords + savedFlashcards.size());
        templateRepository.save(template);

        return savedFlashcards;
    }






    @CacheEvict(value = "templates", key = "'allTemplates'")
    public void deleteFlashcard(Long id_Flashcard){
        if(!templateFlashcardRepository.existsById(id_Flashcard)){
            throw new RuntimeException("Template with id "+ id_Flashcard + "doesnt exist");

        }
        Optional<Template_flashcard> templateFlashcard = templateFlashcardRepository.findById(id_Flashcard);
        if (templateFlashcard.isPresent()) {
            Template template = templateFlashcard.get().getTemplate();

            int currentTotalWords = template.getTotalWords();
            template.setTotalWords(currentTotalWords - 1);
            templateRepository.save(template);
        }


        templateFlashcardRepository.deleteById(id_Flashcard);
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
