package com.idealista.services.impl;

import com.idealista.services.InMemoryService;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;

import java.util.List;

public class InMemoryServiceImpl implements InMemoryService {

    @Override
    public int calculateScore(AdVO ad, List<PictureVO> pictures) {
        int score = 0;
        if (pictures != null && !pictures.isEmpty()) {
            for (PictureVO picture : pictures) {
                if (picture.getQuality().equals("HD")) {
                    score += 20;
                } else {
                    score += 10;
                }
            }
        } else {
            score -= 10;
        }
        if(ad.getDescription() != null && !ad.getDescription().isEmpty()){
            score += 5;
            String lowerCaseDescription = ad.getDescription().toLowerCase();
            int adDescriptionLength = lowerCaseDescription.split(" ").length;
            if (adDescriptionLength >= 20 && adDescriptionLength <= 49) {
                score += 10;
            }
            if (adDescriptionLength >= 50) {
                if (ad.getTypology().equals("FLAT")) {
                    score += 30;
                } else if (ad.getTypology().equals("CHALET")) {
                    score += 20;
                }
            }
            if (lowerCaseDescription.contains("luminoso")) {
                score += 5;
            }
            if (lowerCaseDescription.contains("nuevo")) {
                score += 5;
            }
            if (lowerCaseDescription.contains("céntrico")) {
                score += 5;
            }
            if (lowerCaseDescription.contains("reformado")) {
                score += 5;
            }
            if (lowerCaseDescription.contains("ático")) {
                score += 5;
            }
        }
        score = Math.max(score, 0);
        score = Math.min(score, 100);
        return score;
    }

}
