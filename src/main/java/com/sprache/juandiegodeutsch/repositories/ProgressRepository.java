package com.sprache.juandiegodeutsch.repositories;

import com.sprache.juandiegodeutsch.models.Progress;
import com.sprache.juandiegodeutsch.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress,Long> {
    List<Progress> findByFlashcardIdInAndUser(List<Long> flashcardIds, User user);

    @Query("SELECT p FROM Progress p WHERE p.flashcard.deck.id = :deckId AND p.next_date_review <= :date")
    List<Progress> findByFlashcardDeckAndNextDateReviewBeforeOrEqual(
            @Param("deckId") Long deckId,
            @Param("date") LocalDate date
    );



    @Query(value = "SELECT COUNT(*) FROM progress p WHERE p.id_deck = :deckId AND p.next_review_date <= :date", nativeQuery = true)
    int countToReviewWords(@Param("deckId") Long deckId, @Param("date") LocalDate date);


    @Query(value = "SELECT COUNT(*) FROM progress p WHERE p.id_deck = :deckId AND p.box_number >= :boxNumber", nativeQuery = true)
    int countLearnedWords(@Param("deckId") Long deckId, @Param("boxNumber") int boxNumber);






}




