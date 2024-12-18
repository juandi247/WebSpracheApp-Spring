package com.sprache.juandiegodeutsch.services;


import com.sprache.juandiegodeutsch.dtos.ProgressRequestDTO;
import com.sprache.juandiegodeutsch.models.Progress;
import com.sprache.juandiegodeutsch.models.User;
import com.sprache.juandiegodeutsch.repositories.ProgressRepository;
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
            if (streak >= 12) {
                progress.setBox_number(5);
            } else if (streak >= 10) {
                progress.setBox_number(4);
            } else if (streak >= 8) {
                progress.setBox_number(3);
            } else if (streak >= 5) {
                progress.setBox_number(2);
            } else {
                progress.setBox_number(1);
            }

            LocalDate nextReviewDate = calculateNextReviewDate(progress.getBox_number());
            progress.setNext_date_review(nextReviewDate);

            progress.setLast_date_review(LocalDate.now());
        });

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
                return LocalDate.now(); // Box 1 revisa el mismo día
        }



    }
}
