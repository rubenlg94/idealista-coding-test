package com.idealista.services.impl;

import com.idealista.constants.Values;
import com.idealista.entities.PublicAd;
import com.idealista.entities.QualityAd;
import com.idealista.repositores.InMemoryRepository;
import com.idealista.services.InMemoryService;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public void fillScoresAndIrrelevantSince() {
        List<AdVO> ads = findAllAds();
        for (AdVO ad : ads) {
            List<PictureVO> adPictures = findPicturesByAd(ad.getId());
            ad.setScore(calculateScore(ad, adPictures));
            fillIrrelevantSince(ad);
        }
    }

    @Override
    public void fillIrrelevantSince(AdVO ad) {
        if (ad.getScore() < Values.Ads.MINIMUM_SCORE) {
            ad.setIrrelevantSince(new Date(System.currentTimeMillis()));
        }
    }

    @Override
    public List<QualityAd> qualityListing() {
        List<AdVO> ads = findAllAds();
        return createQualityAdsFromAdVOs(ads);
    }

    private List<QualityAd> createQualityAdsFromAdVOs(List<AdVO> ads) {
        List<QualityAd> qualityAds = new ArrayList<>();
        for (AdVO adVO : ads) {
            QualityAd qualityAd = createQualityAdFromAdVO(adVO);
            qualityAds.add(qualityAd);
        }
        return qualityAds;
    }

    private QualityAd createQualityAdFromAdVO(AdVO adVO) {
        QualityAd qualityAd = new QualityAd();
        qualityAd.setId(adVO.getId());
        qualityAd.setDescription(adVO.getDescription());
        qualityAd.setGardenSize(adVO.getGardenSize());
        qualityAd.setHouseSize(adVO.getHouseSize());
        qualityAd.setIrrelevantSince(adVO.getIrrelevantSince());
        qualityAd.setTypology(adVO.getTypology());
        qualityAd.setScore(adVO.getScore());
        qualityAd.setPictureUrls(findPicturesByAd(adVO.getId()).stream().map(PictureVO::getUrl).collect(Collectors.toList()));
        return qualityAd;
    }

    @Override
    public List<PublicAd> publicListing() {
        List<AdVO> ads = inMemoryRepository.findRelevantAds();
        return createPublicAdsFromAdVOs(ads);
    }

    private List<PublicAd> createPublicAdsFromAdVOs(List<AdVO> ads) {
        List<PublicAd> publicAds = new ArrayList<>();
        for (AdVO adVO : ads) {
            PublicAd publicAd = createPublicAdFromAdVO(adVO);
            publicAds.add(publicAd);
        }
        return publicAds;
    }

    private PublicAd createPublicAdFromAdVO(AdVO adVO) {
        PublicAd publicAd = new PublicAd();
        publicAd.setId(adVO.getId());
        publicAd.setDescription(adVO.getDescription());
        publicAd.setGardenSize(adVO.getGardenSize());
        publicAd.setHouseSize(adVO.getHouseSize());
        publicAd.setTypology(adVO.getTypology());
        publicAd.setPictureUrls(findPicturesByAd(adVO.getId()).stream().map(PictureVO::getUrl).collect(Collectors.toList()));
        return publicAd;
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
