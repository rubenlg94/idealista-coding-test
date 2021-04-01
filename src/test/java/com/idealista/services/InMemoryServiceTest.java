package com.idealista.services;

import com.idealista.services.impl.InMemoryServiceImpl;
import com.idealista.utilities.Utilities;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        puntuations = Utilities.getManuallyCalculatedScores();
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
    public void increaseScoreIfAdHasPictures(){
        AdVO adWithHdAndSDPictures = new AdVO(1, "", "", Arrays.asList(1, 2), 0, 0, 0, null);
        AdVO adWithHdPictures = new AdVO(2, "", "", Collections.singletonList(1), 0, 0, 0, null);
        AdVO adWithSdPictures = new AdVO(3, "", "", Collections.singletonList(2), 0, 0, 0, null);
        AdVO adWithoutPictures = new AdVO(4, "", "", Collections.emptyList(), 0, 0, 0, null);
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");
        PictureVO pictureSd = new PictureVO(2, "http://www.idealista.com/pictures/2", "SD");
        assertEquals(inMemoryServiceImpl.calculateScore(adWithHdAndSDPictures, Arrays.asList(pictureHd, pictureSd)), 30);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithHdPictures, Collections.singletonList(pictureHd)), 20);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithSdPictures, Collections.singletonList(pictureSd)), 10);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithoutPictures, Collections.emptyList()), 0);
    }

    @Test
    public void decreaseScoreIfAdHasNoPictures(){
        AdVO adWithoutPictures = new AdVO(1, "", "", Collections.emptyList(), 0, 0, 0, null);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithoutPictures, Collections.emptyList()), 0);
    }




}
