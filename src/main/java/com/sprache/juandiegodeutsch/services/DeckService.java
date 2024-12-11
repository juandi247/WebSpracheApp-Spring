package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.dtos.DeckRequestDTO;
import com.sprache.juandiegodeutsch.models.Deck;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.repositories.DeckRepository;
import com.sprache.juandiegodeutsch.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeckService {


private final DeckRepository deckRepository;
private final UserRepository userRepository;


    //Method to create a category

    @Transactional
    public Deck createDeck(DeckRequestDTO request, User user) {

        if (deckRepository.findByNameAndUser(request.getName(),user).isPresent()) {
            throw new RuntimeException("Category with this name already exists");
        }

        Deck deck=new Deck();
        deck.setName(request.getName());
        deck.setDescription(request.getDescription());
        deck.setCreation_date(LocalDateTime.now());
        deck.setUser(user);

        return deckRepository.save(deck);
    }






    public void deleteDeck(Long id, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deck not found"));

        if (!deck.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this note");
        }

        deckRepository.delete(deck);
    }

}


