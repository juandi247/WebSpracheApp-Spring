package com.sprache.juandiegodeutsch.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
public class Minigame_Word_RequestDTO {


    private Map<String, String> wordsWithArticles;

    private String category;
}
