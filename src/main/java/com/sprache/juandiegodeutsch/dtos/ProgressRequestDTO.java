package com.sprache.juandiegodeutsch.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProgressRequestDTO {
    private List<FlashcardReview> reviews;

    @Data
    public static class FlashcardReview {

        @NotNull
        private Long idFlashcard;  // ID from reviewed flashcard
        @NotNull
        private Boolean correct;  // answer
    }
}
