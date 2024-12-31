package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.dtos.DeckRequestDTO;
import com.sprache.juandiegodeutsch.dtos.GetDecksResponseDTO;
import com.sprache.juandiegodeutsch.models.Deck;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.repositories.DeckRepository;
import com.sprache.juandiegodeutsch.repositories.ProgressRepository;
import com.sprache.juandiegodeutsch.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeckService {


private final DeckRepository deckRepository;
private final ProgressRepository progressRepository;





@Cacheable(value = "decks", key = "#user.id")
public List<GetDecksResponseDTO> getUserDecks(User user) {
    System.out.println("Fetching from DB for user ID: " + user.getId());

    List<Deck> decks = deckRepository.findByUser(user);

    return decks.stream()
            .map(deck -> {
                int toReview = progressRepository.countToReviewWords(deck.getId(), LocalDate.now());

                int learnedWords = progressRepository.countLearnedWords(deck.getId(), 4);

                return new GetDecksResponseDTO(
                        deck.getId(),
                        deck.getName(),
                        deck.getDescription(),
                        deck.getTotalWords(),
                        toReview,
                        learnedWords
                );
            })
            .collect(Collectors.toList());
}





    //Method to create a category
  @CacheEvict(value = "decks", key = "#user.id")
    @Transactional
    public Deck createDeck(DeckRequestDTO request, User user) {

        if (deckRepository.findByNameAndUser(request.getName(),user).isPresent()) {
            throw new RuntimeException(" this name already exists");
        }

        Deck deck=new Deck();
        deck.setName(request.getName());
        deck.setDescription(request.getDescription());
        deck.setCreation_date(LocalDateTime.now());
        deck.setUser(user);
        deck.setToReview(0);
        deck.setTotalWords(0);
        deck.setLearnedWords(0);

        return deckRepository.save(deck);
    }








 @CacheEvict(value = "decks", key = "#user.id") // Invalida el caché del usuario
    public void deleteDeck(Long id, User user){


        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        if (!deck.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this deck");
        }

        deckRepository.delete(deck);
    }

}


