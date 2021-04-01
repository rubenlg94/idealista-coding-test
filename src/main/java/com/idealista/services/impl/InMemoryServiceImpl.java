package com.idealista.services.impl;

import com.idealista.services.InMemoryService;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;

import java.util.List;

public class InMemoryServiceImpl implements InMemoryService {

    @Override
    public int calculateScore(AdVO ad, List<PictureVO> pictures) {
        int score = 0;
        score = checkAdPictures(ad, pictures, score);
        score = checkAdDescription(ad, score);

        score = Math.max(score, 0);
        score = Math.min(score, 100);
        return score;
    }

    public int checkAdPictures(AdVO ad, List<PictureVO> pictures, int currentScore) {
        if (pictures != null && !pictures.isEmpty()) {
            for (PictureVO picture : pictures) {
                if (picture.getQuality().equals("HD")) {
                    currentScore += 20;
                } else {
                    currentScore += 10;
                }
            }
        } else {
            currentScore -= 10;
        }
        return currentScore;
    }

    public int checkAdDescription(AdVO ad, int currentScore) {
        if (ad.getDescription() != null && !ad.getDescription().isEmpty()) {
            currentScore += 5;
            currentScore = checkAdDescriptionLength(ad, currentScore);
            currentScore = checkAdDescriptionAwardedWords(ad, currentScore);
        }
        return currentScore;
    }

    public int checkAdDescriptionLength(AdVO ad, int currentScore) {
        if (ad.getTypology().equals("FLAT") || ad.getTypology().equals("CHALET")) {
            String adDescription = ad.getDescription();
            int adDescriptionLength = adDescription.split(" ").length;
            if (adDescriptionLength >= 20 && adDescriptionLength <= 49) {
                currentScore += 10;
            }
            if (adDescriptionLength >= 50) {
                if (ad.getTypology().equals("FLAT")) {
                    currentScore += 30;
                } else if (ad.getTypology().equals("CHALET")) {
                    currentScore += 20;
                }
            }
        }
        return currentScore;
    }

    private int checkAdDescriptionAwardedWords(AdVO ad, int currentScore) {
        String lowerCaseDescription = ad.getDescription().toLowerCase();
        if (lowerCaseDescription.contains("luminoso")) {
            currentScore += 5;
        }
        if (lowerCaseDescription.contains("nuevo")) {
            currentScore += 5;
        }
        if (lowerCaseDescription.contains("céntrico")) {
            currentScore += 5;
        }
        if (lowerCaseDescription.contains("reformado")) {
            currentScore += 5;
        }
        if (lowerCaseDescription.contains("ático")) {
            currentScore += 5;
        }
        return currentScore;
    }

}
