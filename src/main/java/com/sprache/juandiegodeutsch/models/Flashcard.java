package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;

@Entity
@Table(name= "Flaschards")
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


    //Relation with progress

    @OneToOne(mappedBy = "flashcard")
    private Progress progress;




}
