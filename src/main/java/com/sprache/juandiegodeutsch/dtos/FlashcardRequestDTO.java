package com.sprache.juandiegodeutsch.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Data
@Getter
@Setter
public class FlashcardRequestDTO {
    private String deck;
    private Map<String, FlashcardData> flashcardsmap;



    @Data
    @Getter
    @Setter
    public static class FlashcardData {
        private String reverse;
        private String audio;
    }
}
