package com.sprache.juandiegodeutsch.repositories;

import com.sprache.juandiegodeutsch.models.Deck;
import com.sprache.juandiegodeutsch.models.Template;
import com.sprache.juandiegodeutsch.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DeckRepository extends JpaRepository<Deck,Long> {

    Optional<Deck> findByNameAndUser(String name, User user);
    List<Deck> findByUser(User user);

    Optional<Deck> findByIdAndUser(Long id, User user);

}
