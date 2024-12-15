package com.sprache.juandiegodeutsch.repositories;

import com.sprache.juandiegodeutsch.models.Deck;
import com.sprache.juandiegodeutsch.models.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findByDeck(Deck deck);
}
