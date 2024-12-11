package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Decks")
@Getter
@Setter
public class Deck {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime creation_date;


    //Relations

    //Relation with users table
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    //Relation with flashcards table
    @OneToMany(mappedBy = "deck",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flashcard> flashcards;

}
