package com.sprache.juandiegodeutsch.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@Getter
@Setter
public class Template_FlashcardRequestDTO {
    private Long id_template;
    private Map<String, FlashcardData> template_flashcards;



    @Data
    @Getter
    @Setter
    public static class FlashcardData {
        private String reverse;
        private String audio;
    }

}
