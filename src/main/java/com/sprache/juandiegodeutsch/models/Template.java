package com.sprache.juandiegodeutsch.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;



@Entity
@Table
@Data
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime creation_date;


    //Relations


    //Relation with template_flashcards table
    @OneToMany(mappedBy = "template")
    private List<Template_flashcard> template_flashcards;


}
