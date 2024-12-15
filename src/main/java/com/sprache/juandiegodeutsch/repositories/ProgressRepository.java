package com.sprache.juandiegodeutsch.repositories;

import com.sprache.juandiegodeutsch.models.Progress;
import com.sprache.juandiegodeutsch.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress,Long> {
    List<Progress> findByFlashcardIdInAndUser(List<Long> flashcardIds, User user);
}
