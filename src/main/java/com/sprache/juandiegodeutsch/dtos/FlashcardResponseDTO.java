package com.sprache.juandiegodeutsch.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlashcardResponseDTO {
    private Long id;
    private String front;
    private String reverse;
    private String audio; // Si hay algún otro dato adicional, también lo puedes agregar
}

