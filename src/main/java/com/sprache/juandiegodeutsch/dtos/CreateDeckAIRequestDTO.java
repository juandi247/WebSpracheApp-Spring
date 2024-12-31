package com.sprache.juandiegodeutsch.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class CreateDeckAIRequestDTO {

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "description cannot be empty")
    private String description;

    private Map<String, FlashcardRequestDTO.FlashcardData> flashcardsmap;

    @Data
    @Getter
    @Setter
    public static class FlashcardData {
        private String reverse;
        private String audio;
    }
}
