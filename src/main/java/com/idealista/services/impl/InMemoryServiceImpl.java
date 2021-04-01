package com.idealista.services.impl;

import com.idealista.repositores.InMemoryRepository;
import com.idealista.services.InMemoryService;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InMemoryServiceImpl implements InMemoryService {

    private final InMemoryRepository inMemoryRepository;

    public InMemoryServiceImpl(InMemoryRepository inMemoryRepository) {
        this.inMemoryRepository = inMemoryRepository;
    }

    @Override
    public List<AdVO> findAllAds() {
        return inMemoryRepository.findAllAds();
    }

    @Override
    public List<PictureVO> findAllPictures() {
        return inMemoryRepository.findAllPictures();
    }

    @Override
    public List<PictureVO> findPicturesByAd(int adId) {
        return inMemoryRepository.findPicturesByAd(adId);
    }

    @Override
    public int calculateScore(AdVO ad, List<PictureVO> pictures) {
        int score = 0;
        score += checkAdPictures(pictures);
        score += checkAdDescription(ad);
        score += checkIfAdIsComplete(ad, pictures);

        score = Math.max(score, 0);
        score = Math.min(score, 100);
        return score;
    }

    public int checkAdPictures(List<PictureVO> pictures) {
        int toIncrease = 0;
        if (pictures != null && !pictures.isEmpty()) {
            for (PictureVO picture : pictures) {
                if (picture.getQuality().equals("HD")) {
                    toIncrease += 20;
                } else {
                    toIncrease += 10;
                }
            }
        } else {
            toIncrease -= 10;
        }
        return toIncrease;
    }

    public int checkAdDescription(AdVO ad) {
        int toIncrease = 0;
        if (ad.getDescription() != null && !ad.getDescription().isEmpty()) {
            toIncrease += 5;
            toIncrease += checkAdDescriptionLength(ad);
            toIncrease += checkAdDescriptionAwardedWords(ad);
        }
        return toIncrease;
    }

    public int checkAdDescriptionLength(AdVO ad) {
        int toIncrease = 0;
        if (ad.getTypology().equals("FLAT") || ad.getTypology().equals("CHALET")) {
            String adDescription = ad.getDescription();
            int adDescriptionLength = adDescription.split(" ").length;
            if (adDescriptionLength >= 20 && adDescriptionLength <= 49) {
                toIncrease += 10;
            }
            if (adDescriptionLength >= 50) {
                if (ad.getTypology().equals("FLAT")) {
                    toIncrease += 30;
                } else if (ad.getTypology().equals("CHALET")) {
                    toIncrease += 20;
                }
            }
        }
        return toIncrease;
    }

    private int checkAdDescriptionAwardedWords(AdVO ad) {
        int toIncrease = 0;
        String lowerCaseDescription = ad.getDescription().toLowerCase();
        if (lowerCaseDescription.contains("luminoso")) {
            toIncrease += 5;
        }
        if (lowerCaseDescription.contains("nuevo")) {
            toIncrease += 5;
        }
        if (lowerCaseDescription.contains("céntrico")) {
            toIncrease += 5;
        }
        if (lowerCaseDescription.contains("reformado")) {
            toIncrease += 5;
        }
        if (lowerCaseDescription.contains("ático")) {
            toIncrease += 5;
        }
        return toIncrease;
    }

    private int checkIfAdIsComplete(AdVO ad, List<PictureVO> pictures) {
        int toIncrease = 0;
        if (pictures != null && !pictures.isEmpty()) {
            switch (ad.getTypology()) {
                case "CHALET":
                    if (ad.getDescription() != null && !ad.getDescription().isEmpty()
                            && ad.getHouseSize() != null && ad.getHouseSize() != 0
                            && ad.getGardenSize() != null && ad.getGardenSize() != 0) {
                        toIncrease += 40;
                    }
                    break;
                case "FLAT":
                    if (ad.getDescription() != null && !ad.getDescription().isEmpty()
                            && ad.getHouseSize() != null && ad.getHouseSize() != 0) {
                        toIncrease += 40;
                    }
                    break;
                case "GARAGE":
                    toIncrease += 40;
                    break;
            }
        }
        return toIncrease;
    }


}
