package com.idealista.services;

import com.idealista.services.impl.InMemoryServiceImpl;
import com.idealista.utilities.Utilities;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class InMemoryServiceTest {

    private List<AdVO> ads;
    private List<PictureVO> pictures;
    private int puntuations[];

    @InjectMocks
    private InMemoryServiceImpl inMemoryServiceImpl;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        ads = Utilities.generateTestAds();
        pictures = Utilities.generateTestPictures();
        puntuations = Utilities.generateScores();
    }

    @Test
    public void scoreIsBetween0And100(){
        for(AdVO ad : ads){
            List<PictureVO> adPictures = pictures.stream().filter(pictureVO -> ad.getPictures() != null && ad.getPictures().contains(pictureVO.getId())).collect(Collectors.toList());
            int adScore = inMemoryServiceImpl.calculateScore(ad, adPictures);
            assertTrue(adScore >= 0 && adScore <= 100);
        }
    }

    @Test
    public void scorePointsIfAdHasPictures(){
        int manuallyCalculatedScores[] = {0, 20, 20, 10, 30, 10, 0, 20};
        for (int i = 0; i < ads.size(); i++) {
            AdVO ad = ads.get(i);
            List<PictureVO> adPictures = pictures.stream().filter(pictureVO -> ad.getPictures() != null && ad.getPictures().contains(pictureVO.getId())).collect(Collectors.toList());
            int adScore = inMemoryServiceImpl.calculateScore(ad, adPictures);
            assertEquals(adScore, manuallyCalculatedScores[i]);
        }
    }

}
