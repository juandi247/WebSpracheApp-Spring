package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name= "Flaschards")
@Getter
@Setter
public class Flashcard {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    private String front;

    private String reverse;

    private String audio;


    //Relations

    //Relation with Deck table
    @ManyToOne
    @JoinColumn(name = "id_deck")
    private Deck deck;


    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    //Relation with progress




    @OneToOne(mappedBy = "flashcard", cascade = CascadeType.ALL, orphanRemoval = true)
    private Progress progress;




}
