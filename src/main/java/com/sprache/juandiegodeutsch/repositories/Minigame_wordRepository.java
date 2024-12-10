package com.sprache.juandiegodeutsch.repositories;

import com.sprache.juandiegodeutsch.models.Category;
import com.sprache.juandiegodeutsch.models.Minigame_word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Minigame_wordRepository extends JpaRepository<Minigame_word, Long> {
    List<Minigame_word> findWordsByCategory(Category category);


}
