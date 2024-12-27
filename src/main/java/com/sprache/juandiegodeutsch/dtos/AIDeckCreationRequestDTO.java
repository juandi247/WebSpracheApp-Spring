package com.sprache.juandiegodeutsch.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AIDeckCreationRequestDTO {

        String reverseLanguage;
         String topic;
         String numberOfFlashcards;
         String typeOfContent;
         String level;
    }
