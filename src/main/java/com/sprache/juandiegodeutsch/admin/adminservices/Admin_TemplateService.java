package com.sprache.juandiegodeutsch.admin.adminservices;


import com.sprache.juandiegodeutsch.dtos.Minigame_Word_RequestDTO;
import com.sprache.juandiegodeutsch.dtos.TemplateRequestDTO;
import com.sprache.juandiegodeutsch.dtos.Template_FlashcardRequestDTO;
import com.sprache.juandiegodeutsch.models.Category;
import com.sprache.juandiegodeutsch.models.Minigame_word;
import com.sprache.juandiegodeutsch.models.Template;
import com.sprache.juandiegodeutsch.models.Template_flashcard;
import com.sprache.juandiegodeutsch.repositories.TemplateRepository;
import com.sprache.juandiegodeutsch.repositories.Template_FlashcardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Admin_TemplateService {


    private final TemplateRepository templateRepository;
    private final Template_FlashcardRepository templateFlashcardRepository;



    //Templates
    @Transactional
    public Template createTemplate(TemplateRequestDTO request) {

        if (templateRepository.existsByName(request.getName())) {
            throw new IllegalStateException("Name of the template already exists");
        }

        Template template = new Template(); // Usar el constructor por defecto
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setCreation_date(LocalDateTime.now());

        return templateRepository.save(template);
    }





    public void deleteTemplate(Long id_template){
       if(!templateRepository.existsById(id_template)){
           throw new RuntimeException("Template with id "+ id_template + "doesnt exist");

       }
        templateRepository.deleteById(id_template);
    }





    //FLASHCARDS

    @Transactional
public List<Template_flashcard> createTemplateFlashcards(Template_FlashcardRequestDTO request) {


        Template template = templateRepository.findByName(request.getTemplate())
                .orElseThrow(() -> new IllegalStateException("Template with name " + request.getTemplate() + " does not exist"));


        List<Template_flashcard> newFlashcards = new ArrayList<>();



        request.getTemplate_flashcards().forEach((front, flashcardData) -> {
            Template_flashcard flashcard = new Template_flashcard();

            flashcard.setFront(front);

            flashcard.setReverse(flashcardData.getReverse());

            flashcard.setAudio(flashcardData.getAudio());

            flashcard.setTemplate(template);

            newFlashcards.add(flashcard);
        });

        return templateFlashcardRepository.saveAll(newFlashcards);
    }





    public void deleteFlashcard(Long id_Flashcard){
        if(!templateFlashcardRepository.existsById(id_Flashcard)){
            throw new RuntimeException("Template with id "+ id_Flashcard + "doesnt exist");

        }
        templateFlashcardRepository.deleteById(id_Flashcard);
    }






}
