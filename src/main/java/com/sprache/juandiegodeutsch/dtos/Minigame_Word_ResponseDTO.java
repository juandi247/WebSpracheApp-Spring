package com.sprache.juandiegodeutsch.dtos;


import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
public class Minigame_Word_ResponseDTO {

    private Map<String, String> wordsAndArticles;

    private String category;
}
