package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Minigame_Category {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    private String category;

    @OneToMany(mappedBy = "minigameCategory")
    private List<Minigame_word> minigameWords;

}
