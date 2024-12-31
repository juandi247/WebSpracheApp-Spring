package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.dtos.FlashcardResponseDTO;
import com.sprache.juandiegodeutsch.dtos.ProgressRequestDTO;
import com.sprache.juandiegodeutsch.models.Deck;
import com.sprache.juandiegodeutsch.models.Flashcard;
import com.sprache.juandiegodeutsch.models.Progress;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.repositories.DeckRepository;
import com.sprache.juandiegodeutsch.repositories.ProgressRepository;
import com.sprache.juandiegodeutsch.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {



private final ProgressRepository progressRepository;
private final DeckRepository deckRepository;
private final UserRepository userRepository;
private final FlashcardService flashcardService;



    public List<FlashcardResponseDTO> getFlashcardsToReviewByDeck(Long deckId, String username) {

        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Deck not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!deck.getUser().equals(user)) {
            throw new RuntimeException("This deck does not belong to the user");
        }

        LocalDate today = LocalDate.now();
        List<Progress> progresses = progressRepository.findByFlashcardDeckAndNextDateReviewBeforeOrEqual(deckId, today);

        return progresses.stream()
                .map(progress -> {
                    Flashcard flashcard = progress.getFlashcard();
                    return new FlashcardResponseDTO(
                            flashcard.getId(),
                            flashcard.getFront(),
                            flashcard.getReverse(),
                            flashcard.getAudio()
                    );
                })
                .collect(Collectors.toList());
    }









@Transactional
    public void reviewProgress(ProgressRequestDTO request, User user){

        List<Long> flashcardIds = request.getReviews().stream()
                .map(ProgressRequestDTO.FlashcardReview::getIdFlashcard)
                .collect(Collectors.toList());

        // Loead progress from ids from the flaschards
        List<Progress> progresses = progressRepository.findByFlashcardIdInAndUser(flashcardIds, user);

        // Map the answers
        Map<Long, Boolean> reviewMap = request.getReviews().stream()
                .collect(Collectors.toMap(
                        ProgressRequestDTO.FlashcardReview::getIdFlashcard,
                        ProgressRequestDTO.FlashcardReview::getCorrect
                ));

        // Update progress
        progresses.forEach(progress -> {
            Boolean isCorrect = reviewMap.get(progress.getFlashcard().getId());

            if (isCorrect == null) {
                throw new IllegalStateException("Flashcard review not found for progress");
            }

            // UPDATE STREAK if its correct
            if (isCorrect) {
                progress.setCorrect_streak(progress.getCorrect_streak() + 1);
            }
            else
            {if (progress.getCorrect_streak() > 1) {
                progress.setCorrect_streak(Math.max(progress.getCorrect_streak() - 2, 0));
            }
            }

            //Update box depending on STREAK
            int streak = progress.getCorrect_streak();
            if (streak >= 10) {
                progress.setBox_number(5);
            } else if (streak >= 7) {
                progress.setBox_number(4);
            } else if (streak >= 5) {
                progress.setBox_number(3);
            } else if (streak >= 3) {
                progress.setBox_number(2);
            } else {
                progress.setBox_number(1);
            }

            LocalDate nextReviewDate = calculateNextReviewDate(progress.getBox_number());
            progress.setNext_date_review(nextReviewDate);

            progress.setLast_date_review(LocalDate.now());
        });

        flashcardService.evictDeckCache(user.getId());

        progressRepository.saveAll(progresses);
    }



    // Method to calculate de next review date based on number of the box
    private LocalDate calculateNextReviewDate(int boxNumber) {
        switch (boxNumber) {
            case 2:
                return LocalDate.now().plusDays(2);
            case 3:
                return LocalDate.now().plusDays(4);
            case 4:
                return LocalDate.now().plusWeeks(1);
            case 5:
                return LocalDate.now().plusWeeks(2);
            default:
                return LocalDate.now();
        }



    }
}
