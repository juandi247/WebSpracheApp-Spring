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
import org.springframework.stereotype.Service;

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


















  //  @Cacheable(value = "flashcards", key = "#deckId + '-' + #username")
    public List<FlashcardResponseDTO> getFlashcardsByDeck(Long deckId, String username) {

        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));



        if (!deck.getUser().equals(user)) {
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







 //   @CacheEvict(value = "flashcards", key = "#request.getDeck_id() + '-' + #user.getUsername()")
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
            progress.setLast_date_review(null); // No se ha revisado aún
            progress.setNext_date_review(LocalDate.now()); // Fecha actual
            progress.setUser(user);
            progress.setFlashcard(flashcard);
            progressRepository.save(progress); // Guardar el progreso
        });
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


      //  evictFlashcardCache(flashcard.getDeck().getId(), username);
        flashcardRepository.delete(flashcard);
    }










    //COPY DECK
    @Transactional
    public void copyTemplateToUser(Long templateId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));


        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalStateException("Template not found"));


        Deck newDeck = new Deck();
        newDeck.setName(template.getName() + "_copied");
        newDeck.setDescription(template.getDescription());
        newDeck.setUser(user);
        deckRepository.save(newDeck);

        // Copiar las flashcards asociadas a la plantilla
        List<Flashcard> copiedFlashcards = template.getTemplate_flashcards().stream()
                .map(templateFlashcard -> {
                    Flashcard flashcard = new Flashcard();
                    flashcard.setFront(templateFlashcard.getFront());
                    flashcard.setReverse(templateFlashcard.getReverse());
                    flashcard.setAudio(templateFlashcard.getAudio());
                    flashcard.setDeck(newDeck);  // Asociamos la flashcard al nuevo mazo
                    flashcard.setUser(user);  // Asociamos la flashcard al usuario
                    return flashcard;
                })
                .collect(Collectors.toList());

        flashcardRepository.saveAll(copiedFlashcards);
}




    private void evictFlashcardCache(Long deckId, String username) {
        // Obtenemos el cache específico del "deckId" y "username"
        String cacheKey = deckId + "-" + username;
        Cache cache = cacheManager.getCache("flashcards");
        if (cache != null) {
            cache.evict(cacheKey); // Eliminamos la entrada del caché
        }
    }




}
