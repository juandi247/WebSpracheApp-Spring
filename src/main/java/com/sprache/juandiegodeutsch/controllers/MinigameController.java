package com.sprache.juandiegodeutsch.controllers;


import com.sprache.juandiegodeutsch.dtos.Minigame_Word_ResponseDTO;
import com.sprache.juandiegodeutsch.services.MinigameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/minigame")
@RequiredArgsConstructor
public class MinigameController {

    private final MinigameService minigameService;

    @GetMapping("/getwords/{category}")
    public ResponseEntity<Minigame_Word_ResponseDTO> getWordsByCategory(@PathVariable String category ){

        Minigame_Word_ResponseDTO response=minigameService.getwordsByCategory(category);

        return ResponseEntity.ok(response);
    }
}
