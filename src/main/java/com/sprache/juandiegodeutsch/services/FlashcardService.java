package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.dtos.FlashcardRequestDTO;
import com.sprache.juandiegodeutsch.dtos.FlashcardResponseDTO;
import com.sprache.juandiegodeutsch.models.*;
import com.sprache.juandiegodeutsch.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.sprache.juandiegodeutsch.dtos.EditFlashcardRequestDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashcardService {


    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    private final ProgressRepository progressRepository;
    private final CacheManager cacheManager;


















  @Cacheable(value = "flashcards", key = "#deckId + '-' + #user.id")
    public List<FlashcardResponseDTO> getFlashcardsByDeck(Long deckId, User user) {

        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));



      if (!deck.getUser().getId().equals(user.getId())) {
            System.out.println(deck.getUser());
            System.out.println("AAAAAAAAAAAAAAA");
            System.out.println(user);
            throw new RuntimeException("This deck does not belong to the user");
        }


        List<Flashcard> flashcards = flashcardRepository.findByDeck(deck);


        return flashcards.stream()
                .map(flashcard -> new FlashcardResponseDTO(
                        flashcard.getId(),
                        flashcard.getFront(),
                        flashcard.getReverse(),
                        flashcard.getAudio()))
                .collect(Collectors.toList());
    }







    @Transactional
    public List<Flashcard> createFlashcard(FlashcardRequestDTO request, User user) {

        Deck deck = deckRepository.findByIdAndUser((long) request.getDeck_id(), user)
                .orElseThrow(() -> new IllegalStateException("Deck with name " + request.getDeck_id() + " does not exist"));


        List<Flashcard> newFlashcards = request.getFlashcardsmap().entrySet().stream()
                .map(entry -> {
                    Flashcard flashcard = new Flashcard();
                    flashcard.setFront(entry.getKey());
                    flashcard.setReverse(entry.getValue().getReverse());
                    flashcard.setAudio(entry.getValue().getAudio());
                    flashcard.setDeck(deck);
                    flashcard.setUser(user);
                    return flashcard;

                })
                .collect(Collectors.toList());


        List<Flashcard> savedFlashcards=flashcardRepository.saveAll(newFlashcards);

        savedFlashcards.forEach(flashcard -> {

            Progress progress = new Progress();
            progress.setBox_number(1);
            progress.setCorrect_streak(0);
            progress.setLast_date_review(null); //
            progress.setNext_date_review(LocalDate.now()); // Fec
            progress.setUser(user);
            progress.setDeck(deck);
            progress.setFlashcard(flashcard);
            progressRepository.save(progress); // Guardar el progreso
        });

        int newFlashcardsCount = savedFlashcards.size();
        deck.setTotalWords(deck.getTotalWords() + newFlashcardsCount);
        deckRepository.save(deck);


        evictDeckCache(user.getId());
        evictFlashcardsCache(deck.getId(), user.getId());


        return savedFlashcards;
    }






    public void deleteFlashcard(Long id, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        if (!flashcard.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this note");
        }


        Deck deck = flashcard.getDeck();
        deck.setTotalWords(deck.getTotalWords() - 1);
        deckRepository.save(deck);


        //CACHE EVICT
        evictDeckCache(user.getId());
        evictFlashcardsCache(deck.getId(), user.getId());

        flashcardRepository.delete(flashcard);
    }



    public void editFlashcard(Long id, EditFlashcardRequestDTO requestDTO,String username ){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        if (!flashcard.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to edit this note");
        }



        if (requestDTO.getFront() != null) {
            flashcard.setFront(requestDTO.getFront());
        }
        if (requestDTO.getReverse() != null) {
            flashcard.setReverse(requestDTO.getReverse());
        }
        if (requestDTO.getAudio() != null) {
            flashcard.setAudio(requestDTO.getAudio());
        }
        Deck deck = flashcard.getDeck();
        evictFlashcardsCache(deck.getId(), user.getId());



         flashcardRepository.save(flashcard);




    }















    //COPY DECK
    @Transactional
    public void copyTemplateToUser(Long templateId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));


        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalStateException("Template not found"));


        Deck newDeck = new Deck();
        newDeck.setName(template.getName() + "_Copied");
        newDeck.setDescription(template.getDescription());
        newDeck.setUser(user);
        newDeck.setTotalWords(template.getTotalWords());
        deckRepository.save(newDeck);



        List<Flashcard> copiedFlashcards = template.getTemplate_flashcards().stream()
                .map(templateFlashcard -> {
                    Flashcard flashcard = new Flashcard();
                    flashcard.setFront(templateFlashcard.getFront());
                    flashcard.setReverse(templateFlashcard.getReverse());
                    flashcard.setAudio(templateFlashcard.getAudio());
                    flashcard.setDeck(newDeck);
                    flashcard.setUser(user);
                    return flashcard;
                })
                .collect(Collectors.toList());

        List<Flashcard> savedFlashcards=flashcardRepository.saveAll(copiedFlashcards);

        savedFlashcards.forEach(flashcard -> {

            Progress progress = new Progress();
            progress.setBox_number(1);
            progress.setCorrect_streak(0);
            progress.setLast_date_review(null); //
            progress.setNext_date_review(LocalDate.now());
            progress.setUser(user);
            progress.setDeck(newDeck);
            progress.setFlashcard(flashcard);
            progressRepository.save(progress);
        });

        evictDeckCache(user.getId());

    }



    private void evictFlashcardsCache(Long deckId, Long userId) {
        String cacheKey = deckId + "-" + userId;

        Cache cache = cacheManager.getCache("flashcards");

        if (cache != null) {
            cache.evict(cacheKey);
        }
    }





    public void evictDeckCache(Long userId) {
        Cache cache = cacheManager.getCache("decks");
        if (cache != null) {
            cache.evict(userId);
        }
    }




}
