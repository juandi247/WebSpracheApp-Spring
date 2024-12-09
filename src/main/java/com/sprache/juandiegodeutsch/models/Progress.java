package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Progress")
public class Progress {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int box_number;

    private LocalDate last_date_review;

    private LocalDate next_date_review;

    private int correct_streak;



    //Relations
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;



    @OneToOne
    @JoinColumn(name = "id_flashcard")
    private Flashcard flashcard;


}
