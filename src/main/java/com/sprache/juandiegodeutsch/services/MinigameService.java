package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.dtos.Minigame_Word_RequestDTO;
import com.sprache.juandiegodeutsch.dtos.Minigame_Word_ResponseDTO;
import com.sprache.juandiegodeutsch.models.Category;
import com.sprache.juandiegodeutsch.models.Minigame_word;
import com.sprache.juandiegodeutsch.repositories.Minigame_wordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MinigameService {



    private final Minigame_wordRepository minigameWordRepository;

    //Method to get words for the admin
    public Minigame_Word_ResponseDTO getwordsByCategory (String category){
        Category categoryEnum= Category.valueOf(category.toUpperCase());

        List<Minigame_word> wordslist= minigameWordRepository.findWordsByCategory(categoryEnum);

        Collections.shuffle(wordslist);
        Map<String,String> wordsAndArticles= wordslist.stream()
                .collect(Collectors.toMap(Minigame_word::getWord,Minigame_word::getArticle));

        return new Minigame_Word_ResponseDTO(wordsAndArticles,categoryEnum.name());
    }



}
