package com.sprache.juandiegodeutsch.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FlashcardResponseDTO implements Serializable {
    private Long id;
    private String front;
    private String reverse;
    private String audio; // Si hay algún otro dato adicional, también lo puedes agregar
}

