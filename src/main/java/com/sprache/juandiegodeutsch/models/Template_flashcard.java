package com.sprache.juandiegodeutsch.models;

import jakarta.persistence.*;

public class Template_flashcard {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String front;

    private String reverse;

    private String audio;


    //Relations

    //Relation with Deck table
    @ManyToOne
    @JoinColumn(name = "id_template")
    private Template template;

}
