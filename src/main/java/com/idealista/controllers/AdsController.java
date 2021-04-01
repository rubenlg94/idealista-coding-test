package com.idealista.controllers;

import java.util.List;

import com.idealista.entities.PublicAd;
import com.idealista.entities.QualityAd;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ads")
public class AdsController {

    @GetMapping("/quality")
    public ResponseEntity<List<QualityAd>> qualityListing() {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/public")
    public ResponseEntity<List<PublicAd>> publicListing() {
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/calculate-score")
    public ResponseEntity<Void> calculateScore() {
        return ResponseEntity.notFound().build();
    }
}
