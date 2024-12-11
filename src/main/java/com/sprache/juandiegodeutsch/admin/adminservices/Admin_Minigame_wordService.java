package com.sprache.juandiegodeutsch.admin.adminservices;


import com.sprache.juandiegodeutsch.dtos.Minigame_Word_RequestDTO;
import com.sprache.juandiegodeutsch.dtos.Minigame_Word_ResponseDTO;
import com.sprache.juandiegodeutsch.models.Category;
import com.sprache.juandiegodeutsch.models.Minigame_word;
import com.sprache.juandiegodeutsch.repositories.Minigame_wordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class Admin_Minigame_wordService {


    private final Minigame_wordRepository minigameWordRepository;




    //Method to get words for the admin
    public Minigame_Word_ResponseDTO getwordsByCategory (String category){
        Category categoryEnum= Category.valueOf(category.toUpperCase());

        List<Minigame_word> wordslist= minigameWordRepository.findWordsByCategory(categoryEnum);
        Map<String,String> wordsAndArticles= wordslist.stream()
                .collect(Collectors.toMap(Minigame_word::getWord,Minigame_word::getArticle));

        return new Minigame_Word_ResponseDTO(wordsAndArticles,categoryEnum.name());
    }







    //Method to create new words based on a category
    @Transactional
    public List<Minigame_word> createWords(Minigame_Word_RequestDTO request) {
        Category categoryEnum;
        try {
            categoryEnum = Category.valueOf(request.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("invalid category " + request.getCategory());
        }



        List<Minigame_word> words = new ArrayList<>();


        request.getWordsWithArticles().forEach((wordText, articleText) -> {
            Minigame_word word = new Minigame_word();
            word.setWord(wordText);
            word.setArticle(articleText);
            word.setCategory(categoryEnum);

            words.add(word);
        });

        return minigameWordRepository.saveAll(words);
    }




    public void deleteWord(Long id_template){
        if(!minigameWordRepository.existsById(id_template)){
            throw new RuntimeException("Word with id "+ id_template + "doesnt exist");

        }
        minigameWordRepository.deleteById(id_template);
    }




}



