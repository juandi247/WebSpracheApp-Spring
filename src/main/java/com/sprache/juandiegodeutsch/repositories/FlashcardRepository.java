package com.sprache.juandiegodeutsch.repositories;

import com.sprache.juandiegodeutsch.models.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
}
