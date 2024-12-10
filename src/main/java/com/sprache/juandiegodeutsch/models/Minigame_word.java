package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;

@Entity
@Table
public class Minigame_word {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    private String word;

    private String article;


    @ManyToOne
    @JoinColumn(name = "id_category")
    private Minigame_Category minigameCategory;
}
