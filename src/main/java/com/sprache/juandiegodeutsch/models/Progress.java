package com.sprache.juandiegodeutsch.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Progress")
@Getter
@Setter
public class Progress {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "BoxNumber")
    private int box_number;

    @Column(name="LastReviewDate")
    private LocalDate last_date_review;


    @Column(name = "NextReviewDate")
    private LocalDate next_date_review;



    @Column(name = "CorrectStreak")
    private int correct_streak;



    //Relations
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;



    @OneToOne
    @JoinColumn(name = "id_flashcard")
    private Flashcard flashcard;


}
