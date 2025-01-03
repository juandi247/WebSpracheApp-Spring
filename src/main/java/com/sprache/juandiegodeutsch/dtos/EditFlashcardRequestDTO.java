package com.sprache.juandiegodeutsch.dtos;


import lombok.Data;

@Data
public class EditFlashcardRequestDTO {
 private String front;
 private String reverse;
 private String audio;
}
