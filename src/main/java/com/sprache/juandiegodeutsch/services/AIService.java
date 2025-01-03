package com.sprache.juandiegodeutsch.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprache.juandiegodeutsch.dtos.AIDeckCreationRequestDTO;
import com.sprache.juandiegodeutsch.dtos.CorrectTextAIRequestDTO;
import com.sprache.juandiegodeutsch.dtos.CreateDeckAIRequestDTO;

import com.sprache.juandiegodeutsch.dtos.InstructionsAIRequestDTO;
import com.sprache.juandiegodeutsch.models.*;
import com.sprache.juandiegodeutsch.repositories.DeckRepository;
import com.sprache.juandiegodeutsch.repositories.FlashcardRepository;
import com.sprache.juandiegodeutsch.repositories.ProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIService {

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final DeckRepository deckRepository;
    private final FlashcardRepository flashcardRepository;
    private final ProgressRepository progressRepository;


    private final String groqApiUrl="https://api.groq.com/openai/v1/chat/completions";

    @Value("${api.key.ai}")
    private String groqApiKey;




    public String correctSentenceInGerman(User user, String sentence) {
        validateUserForAI(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + groqApiKey);
        headers.set("Content-Type", "application/json");

        String content = "I am learning German, and I wrote the following sentence, please correct the following sentence in German: \"" + sentence + "\". " +
                "If the sentence is correct, just say that the sentence is very good, but if it is incorrect " +
                "tell me what went wrong and the corrected sentence, but the message should be very short, saying that it was wrong and the corrected sentence, " +
                "it should not be too long, dont add extra text.";

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-70b-versatile",  // Utilizando el modelo adecuado para corrección
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



            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error with API Groq: " + e.getMessage(), e);
        }
    }





    public String generateInstructions(User user, InstructionsAIRequestDTO requestDTO) {
        validateUserForAI(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + groqApiKey);
        headers.set("Content-Type", "application/json");

        String examples = "";

        switch (requestDTO.getLevel()) {
            case "A1":
                examples = "Sie möchten im August Dresden besuchen. Schreiben Sie an die Touristeninformation: – Warum schreiben Sie? – " +
                        "Bitten Sie: Informationen über Filme, Museen usw. (Kulturprogramm). – Fragen Sie: Hoteladressen?";
                break;
            case "A2":
                examples = "Sie möchten im August Dresden besuchen. Schreiben Sie an die Touristeninformation: – Warum schreiben Sie? – " +
                        "Bitten Sie: Informationen über Filme, Museen usw. (Kulturprogramm). – Fragen Sie: Hoteladressen?";
                break;
            case "B1":
                examples = "Sie haben im Fernsehen eine Diskussionssendung zum Thema „Persönliche Kontakte und Internet“ gesehen.\n" +
                        "Im Online-Gästebuch der Sendung finden Sie folgende Meinung:\n" +
                        "Ihre Kursleiterin, Frau Müller, hat Sie zu einem Gespräch über Ihre persönlichen Lernziele eingeladen.\n" +
                        "Zu dem Termin können Sie aber nicht kommen.\n" +
                        "Schreiben Sie an Frau Müller. Entschuldigen Sie sich höflich und berichten Sie, warum Sie nicht\n" +
                        "kommen können.\n" +
                        "Schreiben Sie eine E-Mail (circa 70 Wörter).\n" +
                        "Vergessen Sie nicht die Anrede und den Gruß am Schluss.\n" +
                        "www.diskussion-aktuell.de\n" +
                        "Ich finde es schlimm, dass persönliche\n" +
                        "Treffen immer seltener werden. Freunde\n" +
                        "wohnen oft sehr weit auseinander. Und\n" +
                        "da ist man dann schon froh über das\n" +
                        "Internet. Aber Kontakte im Internet können\n" +
                        "doch persönliche Treffen nicht ersetzen! ";
                break;
            case "B2":
                examples = "Sie haben im Fernsehen eine Diskussionssendung zum Thema „Persönliche Kontakte und Internet“ gesehen.\n" +
                        "Im Online-Gästebuch der Sendung finden Sie folgende Meinung:\n" +
                        "Ihre Kursleiterin, Frau Müller, hat Sie zu einem Gespräch über Ihre persönlichen Lernziele eingeladen.\n" +
                        "Zu dem Termin können Sie aber nicht kommen.\n" +
                        "Schreiben Sie an Frau Müller. Entschuldigen Sie sich höflich und berichten Sie, warum Sie nicht\n" +
                        "kommen können.\n" +
                        "Schreiben Sie eine E-Mail (circa 70 Wörter).\n" +
                        "Vergessen Sie nicht die Anrede und den Gruß am Schluss.\n" +
                        "Aufgabe 2 Arbeitszeit: 25 Minuten\n" +
                        "www.diskussion-aktuell.de\n" +
                        "Ich finde es schlimm, dass persönliche\n" +
                        "Treffen immer seltener werden. Freunde\n" +
                        "wohnen oft sehr weit auseinander. Und\n" +
                        "da ist man dann schon froh über das\n" +
                        "Internet. Aber Kontakte im Internet können\n" +
                        "doch persönliche Treffen nicht ersetzen!";
                break;
            case "C1":
                examples = "Sie haben im Fernsehen eine Diskussionssendung zum Thema „Persönliche Kontakte und Internet“ gesehen.\n" +
                        "Im Online-Gästebuch der Sendung finden Sie folgende Meinung:\n" +
                        "Ihre Kursleiterin, Frau Müller, hat Sie zu einem Gespräch über Ihre persönlichen Lernziele eingeladen.\n" +
                        "Zu dem Termin können Sie aber nicht kommen.\n" +
                        "Schreiben Sie an Frau Müller. Entschuldigen Sie sich höflich und berichten Sie, warum Sie nicht\n" +
                        "kommen können.\n" +
                        "Schreiben Sie eine E-Mail (circa 40 Wörter).\n" +
                        "Vergessen Sie nicht die Anrede und den Gruß am Schluss.\n" +
                        "Aufgabe 2 Arbeitszeit: 25 Minuten\n" +
                        "www.diskussion-aktuell.de\n" +
                        "Ich finde es schlimm, dass persönliche\n" +
                        "Treffen immer seltener werden. Freunde\n" +
                        "wohnen oft sehr weit auseinander. Und\n" +
                        "da ist man dann schon froh über das\n" +
                        "Internet. Aber Kontakte im Internet können\n" +
                        "doch persönliche Treffen nicht ersetzen!";
                break;
        }



        String content = "Estoy aprendiendo aleman y necesito practicar mi Schreiben. Escogí el siguiente Tema "+requestDTO.getTopic()+
                "Y necesito que me generes las instrucciones como si fueras un profesor, para armar un texto sobre ese tema.Quiero que las instrucciones que generes" +
                "sean acordes con el nivel " + requestDTO.getLevel()+
                "Aca hay un ejemplo de como es en la prueba del goethe para te guies con el nivel escogido:  "+ examples + ". Solo dame las instrucciones con respecto al tema" +
                "y no generes texto adicional. Genera unicamente un conjunto de maximo 3 o 4 instrucicones del tema que te pedí en aleman claramente sin ningun texto ni titutlo adicional. ";

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-70b-versatile",  // Utilizando el modelo adecuado para corrección
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



            return removeQuotes(jsonNode.get("choices").get(0).get("message").get("content").asText());
        } catch (Exception e) {
            throw new RuntimeException("Error with API Groq: " + e.getMessage(), e);
        }
    }



    public String removeQuotes(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\"", "");
    }















    public String correctText(User user, CorrectTextAIRequestDTO requestDTO) {
        validateUserForAI(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + groqApiKey);
        headers.set("Content-Type", "application/json");
        System.out.println(requestDTO.getText());

        String content = "Du bist ein Deutschlehrer. Und ich lerne Deutsch und brauche, dass du meinen Text korrigierst. Das Niveau des Textes, den ich gewählt habe, ist " + requestDTO.getLevel() +
                "Das Thema, das ich gewählt habe, ist: " + requestDTO.getTopic() +
                "Korrigiere die Grammatik, die Rechtschreibfehler und gib mir das Feedback zu dem, was du korrigiert hast" +
                "Alles auf Deutsch. Ich brauche nur die Korrektur ohne zusätzlichen Text, klar und dem Niveau " + requestDTO.getLevel() + " entsprechend und bitte auch die Fehler, die ich hatte und ihre Korrektur." +
                "Hier ist der Text:" + requestDTO.getText();



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

            System.out.println(requestDTO.getText());

            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error with API Groq: " + e.getMessage(), e);
        }
    }

























    public List<Map<String,String>> generateDeckWithAI(User user, AIDeckCreationRequestDTO aiDeckCreationRequestDTO) {
        validateUserForAI(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + groqApiKey);
        headers.set("Content-Type", "application/json");




        String content = "Necesito que generes una tabla de flashcards, con su front en alemán y su reverso en "
                + aiDeckCreationRequestDTO.getReverseLanguage()
                + " para practicar mi alemán. Debes generar "
                + aiDeckCreationRequestDTO.getNumberOfFlashcards()
                + " flashcards en total, y deben ser "
                + aiDeckCreationRequestDTO.getTypeOfContent() +" si son sustantivos ponles el articulo der die das,"
                + ", sobre el tema "
                + aiDeckCreationRequestDTO.getTopic()
                + " y deben ser de nivel "
                + aiDeckCreationRequestDTO.getLevel()
                + ". No generes texto adicional, solo genera la tabla con una estructura tipo lista y enumeradas siguendo la misma estructura la cual debe ser asi"+
                "front: contenido de la palabra,back: contenido del reverso y un espacio en blanco para separar la flashcard siguiente" +
                "sin ningun tipo de titulo,empezando directamente con el contenido de la tabla.";


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










    @Transactional
    public Deck createDeckWithAI(CreateDeckAIRequestDTO request, User user) {

        if (deckRepository.findByNameAndUser(request.getName(), user).isPresent()) {
            throw new RuntimeException("This deck name already exists");
        }

        Deck deck = new Deck();
        deck.setName(request.getName());
        deck.setDescription(request.getDescription());
        deck.setCreation_date(LocalDateTime.now());
        deck.setUser(user);
        Deck savedDeck = deckRepository.save(deck);

        if (request.getFlashcardsmap() != null && !request.getFlashcardsmap().isEmpty()) {
            List<Flashcard> newFlashcards = request.getFlashcardsmap().entrySet().stream()
                    .map(entry -> {
                        Flashcard flashcard = new Flashcard();
                        flashcard.setFront(entry.getKey());
                        flashcard.setReverse(entry.getValue().getReverse());
                        flashcard.setAudio(entry.getValue().getAudio());
                        flashcard.setDeck(savedDeck);
                        flashcard.setUser(user);
                        return flashcard;
                    })
                    .collect(Collectors.toList());

            List<Flashcard> savedFlashcards = flashcardRepository.saveAll(newFlashcards);

            savedFlashcards.forEach(flashcard -> {
                Progress progress = new Progress();
                progress.setBox_number(1);
                progress.setCorrect_streak(0);
                progress.setLast_date_review(null);
                progress.setNext_date_review(LocalDate.now());
                progress.setUser(user);
                progress.setDeck(savedDeck);
                progress.setFlashcard(flashcard);
                progressRepository.save(progress);
            });


            deck.setTotalWords(savedFlashcards.size());
            deckRepository.save(deck);
        }




        return savedDeck;
    }






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
