package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Minigame_word {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    private String word;

    private String article;

    @Enumerated(EnumType.STRING)
    Category category;

}
