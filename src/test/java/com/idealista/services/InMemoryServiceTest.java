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
            int adScore = inMemoryServiceImpl.calculateScore(ad);
            assertTrue(adScore >= 0 && adScore <= 100);
        }
    }

}
