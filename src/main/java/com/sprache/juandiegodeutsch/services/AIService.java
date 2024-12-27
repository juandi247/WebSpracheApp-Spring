package com.sprache.juandiegodeutsch.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprache.juandiegodeutsch.dtos.AIDeckCreationRequestDTO;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.models.UserPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {

    private final RestTemplate restTemplate;
    private final UserService userService;


    private final String groqApiUrl="https://api.groq.com/openai/v1/chat/completions";

    @Value("${api.key.ai}")
    private String groqApiKey;





    public List<Map<String,String>> generateDeckWithAI(User user, AIDeckCreationRequestDTO aiDeckCreationRequestDTO) {
        validateUserForAI(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + groqApiKey);
        headers.set("Content-Type", "application/json");


 //I know that looks a little bit weird but its just for testing

        String content = "Necesito que generes una tabla de flashcards, con su front en alemán y su reverso en "
                + aiDeckCreationRequestDTO.getReverseLanguage()
                + " para practicar mi alemán. Debes generar "
                + aiDeckCreationRequestDTO.getNumberOfFlashcards()
                + " flashcards en total, y deben ser "
                + aiDeckCreationRequestDTO.getTypeOfContent()
                + ", sobre el tema "
                + aiDeckCreationRequestDTO.getTopic()
                + " y deben ser de nivel "
                + aiDeckCreationRequestDTO.getLevel()
                + ". No generes texto adicional, solo genera la tabla con una estructura tipo lista y enumeradas siguendo la misma estructura la cual debe ser asi"+
                "front: contenido de la palabra,back: contenido del reverso y un espacio en blanco para separar la flashcard siguiente" +
                "sin ningun tipo de titulo,empezando directamente con el contenido de la tabla";


        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-70b-versatile",
                "messages", new Object[]{
                        Map.of(
                                "role", "user",
                                "content", content
                        )
                }
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    groqApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );


            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());


            //RETURNS CONTENT
            String responseContent= jsonNode.get("choices").get(0).get("message").get("content").asText();
            return parseFlashcardsFromResponse(responseContent);
        } catch (Exception e) {
            throw new RuntimeException("Error with API Groq: " + e.getMessage(), e);
        }
    }

    public List<Map<String, String>> parseFlashcardsFromResponse(String apiResponse) {
        List<Map<String, String>> flashcards = new ArrayList<>();

        String[] flashcardStrings = apiResponse.split("\n");

        for (String flashcardString : flashcardStrings) {
            String cleanString = flashcardString.replaceAll("^\\d+\\. ", "");

            String[] parts = cleanString.split(", back:");

            if (parts.length == 2) {
                String front = parts[0].replace("front: ", "").trim();
                String back = parts[1].trim();

                Map<String, String> flashcard = new HashMap<>();
                flashcard.put("front", front);
                flashcard.put("back", back);

                flashcards.add(flashcard);
            }
        }

        return flashcards;
    }











    //Validate user plan for ai functionalities

    public void validateUserForAI(User user) {
        if (UserPlan.DEFAULT.equals(user.getUserPlan())) {
            if (user.getAipetitions() < 3) {
                user.setAipetitions(user.getAipetitions() + 1);
                userService.save(user);

            } else {
                throw new RuntimeException("Your free plan only allows you to use AI functions 3 times a day. If you want more, update to a paid plan.");
            }
        }
    }



}
