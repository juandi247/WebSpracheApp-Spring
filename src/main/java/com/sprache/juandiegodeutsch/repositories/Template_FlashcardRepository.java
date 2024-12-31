package com.sprache.juandiegodeutsch.repositories;

import com.sprache.juandiegodeutsch.models.Deck;
import com.sprache.juandiegodeutsch.models.Flashcard;
import com.sprache.juandiegodeutsch.models.Template;
import com.sprache.juandiegodeutsch.models.Template_flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Template_FlashcardRepository extends JpaRepository<Template_flashcard, Long> {

    List<Template_flashcard> findByTemplate(Template template);

}
