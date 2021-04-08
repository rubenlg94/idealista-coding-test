package com.idealista.controllers;

import java.util.List;

import com.idealista.entities.PublicAd;
import com.idealista.entities.QualityAd;
import com.idealista.services.InMemoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private final InMemoryService inMemoryService;

    public AdsController(InMemoryService inMemoryService) {
        this.inMemoryService = inMemoryService;
    }

    @GetMapping("/quality")
    public ResponseEntity<List<QualityAd>> qualityListing() {
        return ResponseEntity.status(HttpStatus.OK).body(inMemoryService.qualityListing());
    }

    @GetMapping("/public")
    public ResponseEntity<List<PublicAd>> publicListing() {
        return ResponseEntity.status(HttpStatus.OK).body(inMemoryService.publicListing());
    }

    @PatchMapping("/calculate-score")
    public ResponseEntity<Void> calculateScore() {
        inMemoryService.scoreAds();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
