package com.idealista.controllers;

import com.idealista.entities.PublicAd;
import com.idealista.entities.QualityAd;
import com.idealista.services.InMemoryService;
import com.idealista.utilities.Utilities;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AdsControllerTest {

    @Mock
    private InMemoryService inMemoryService;

    @InjectMocks
    private AdsController adsController;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testQualityListing() {
        QualityAd qualityAd = new QualityAd();
        List<QualityAd> qualityAds = Collections.singletonList(qualityAd);
        when(inMemoryService.qualityListing()).thenReturn(qualityAds);

        ResponseEntity<List<QualityAd>> response = adsController.qualityListing();

        assertEquals(qualityAds, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testPublicListing() {
        PublicAd publicAd = new PublicAd();
        List<PublicAd> publicAds = Collections.singletonList(publicAd);
        when(inMemoryService.publicListing()).thenReturn(publicAds);

        ResponseEntity<List<PublicAd>> response = adsController.publicListing();

        assertEquals(publicAds, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCalculateScore() {
        ResponseEntity<Void> response = adsController.calculateScore();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
