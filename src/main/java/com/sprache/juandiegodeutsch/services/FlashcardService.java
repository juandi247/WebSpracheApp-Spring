package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.dtos.FlashcardRequestDTO;
import com.sprache.juandiegodeutsch.models.*;
import com.sprache.juandiegodeutsch.repositories.DeckRepository;
import com.sprache.juandiegodeutsch.repositories.FlashcardRepository;
import com.sprache.juandiegodeutsch.repositories.TemplateRepository;
import com.sprache.juandiegodeutsch.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashcardService {


    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;




    @Transactional
    public List<Flashcard> createFlashcard(FlashcardRequestDTO request, User user) {

        Deck deck = deckRepository.findByNameAndUser(request.getDeck(), user)
                .orElseThrow(() -> new IllegalStateException("Deck with name " + request.getDeck() + " does not exist"));


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


        return flashcardRepository.saveAll(newFlashcards);
    }





    public void deleteFlashcard(Long id, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        if (!flashcard.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this note");
        }

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




}
