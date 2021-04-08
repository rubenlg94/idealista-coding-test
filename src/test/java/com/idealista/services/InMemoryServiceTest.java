package com.idealista.services;

import com.idealista.entities.PublicAd;
import com.idealista.entities.QualityAd;
import com.idealista.repositores.InMemoryRepository;
import com.idealista.services.impl.InMemoryServiceImpl;
import com.idealista.utilities.Utilities;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ContextConfiguration
public class InMemoryServiceTest {

    private List<AdVO> ads;
    private List<PictureVO> pictures;

    @Mock
    private InMemoryRepository inMemoryRepository;

    @InjectMocks
    private InMemoryServiceImpl inMemoryServiceImpl;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ads = Utilities.generateTestAds();
        pictures = Utilities.generateTestPictures();
    }

    @Test
    public void testIncreaseScoreIfAdHasPictures() {
        // Arrange
        AdVO adWithHdAndSDPictures = new AdVO(1, "", "", Arrays.asList(1, 2), 0, 0, 0, null);
        AdVO adWithHdPictures = new AdVO(2, "", "", Collections.singletonList(1), 0, 0, 0, null);
        AdVO adWithSdPictures = new AdVO(3, "", "", Collections.singletonList(2), 0, 0, 0, null);
        AdVO adWithoutPictures = new AdVO(4, "", "", Collections.emptyList(), 0, 0, 0, null);
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");
        PictureVO pictureSd = new PictureVO(2, "http://www.idealista.com/pictures/2", "SD");

        // Assert
        assertEquals(inMemoryServiceImpl.calculateScore(adWithHdAndSDPictures, Arrays.asList(pictureHd, pictureSd)), 30);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithHdPictures, Collections.singletonList(pictureHd)), 20);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithSdPictures, Collections.singletonList(pictureSd)), 10);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithoutPictures, Collections.emptyList()), 0);
    }

    @Test
    public void testDecreaseScoreIfAdHasNoPictures() {
        AdVO adWithoutPictures = new AdVO(1, "", "", Collections.emptyList(), 0, 0, 0, null);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithoutPictures, Collections.emptyList()), 0);
    }

    @Test
    public void testIncreaseScoreIfAdHasDescription() {
        AdVO adWithDescription = new AdVO(1, "", "Descripción", Collections.singletonList(1), 0, 0, 0, null);
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");
        assertEquals(inMemoryServiceImpl.calculateScore(adWithDescription, Collections.singletonList(pictureHd)), 25);
    }

    @Test
    public void testIncreaseScoreIfDescriptionHasBetween20And49Words() {
        AdVO flat = new AdVO(1, "FLAT", "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20", Collections.singletonList(1), 0, 0, 0, null);
        AdVO chalet = new AdVO(2, "CHALET", "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20", Collections.singletonList(1), 0, 0, 0, null);
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");

        assertEquals(inMemoryServiceImpl.calculateScore(flat, Collections.singletonList(pictureHd)), 35);
        assertEquals(inMemoryServiceImpl.calculateScore(chalet, Collections.singletonList(pictureHd)), 35);
    }

    @Test
    public void testIncreaseScoreIfDescriptionHas50WordsOrMore() {
        AdVO flat = new AdVO(1, "FLAT", "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50", Collections.singletonList(1), 0, 0, 0, null);
        AdVO chalet = new AdVO(2, "CHALET", "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50", Collections.singletonList(1), 0, 0, 0, null);
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");

        assertEquals(inMemoryServiceImpl.calculateScore(flat, Collections.singletonList(pictureHd)), 55);
        assertEquals(inMemoryServiceImpl.calculateScore(chalet, Collections.singletonList(pictureHd)), 45);
    }

    @Test
    public void testIncreaseScoreIfAdHasAwardedWordsInDescription() {
        // Arrange
        AdVO adWithOneAwardedWord = new AdVO(1, "FLAT", "Luminoso", Collections.singletonList(1), 0, 0, 0, null);
        AdVO adWithTwoAwardedWords = new AdVO(1, "FLAT", "Luminoso, Nuevo", Collections.singletonList(1), 0, 0, 0, null);
        AdVO adWithThreeAwardedWords = new AdVO(1, "FLAT", "Luminoso, Nuevo, Céntrico", Collections.singletonList(1), 0, 0, 0, null);
        AdVO adWithFourAwardedWords = new AdVO(1, "FLAT", "Luminoso, Nuevo, Céntrico, Reformado", Collections.singletonList(1), 0, 0, 0, null);
        AdVO adWithFiveAwardedWords = new AdVO(1, "FLAT", "Luminoso, Nuevo, Céntrico, Reformado, Ático", Collections.singletonList(1), 0, 0, 0, null);
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");

        // Assert
        assertEquals(inMemoryServiceImpl.calculateScore(adWithOneAwardedWord, Collections.singletonList(pictureHd)), 30);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithTwoAwardedWords, Collections.singletonList(pictureHd)), 35);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithThreeAwardedWords, Collections.singletonList(pictureHd)), 40);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithFourAwardedWords, Collections.singletonList(pictureHd)), 45);
        assertEquals(inMemoryServiceImpl.calculateScore(adWithFiveAwardedWords, Collections.singletonList(pictureHd)), 50);
    }

    @Test
    public void testIncreaseScoreIfAdIsComplete() {
        AdVO flat = new AdVO(1, "FLAT", "Descripción", Collections.singletonList(1), 20, 0, 0, null);
        AdVO chalet = new AdVO(2, "CHALET", "Descripción", Collections.singletonList(1), 20, 50, 0, null);
        AdVO garage = new AdVO(2, "GARAGE", null, Collections.singletonList(1), 0, 0, 0, null);
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");

        assertEquals(inMemoryServiceImpl.calculateScore(flat, Collections.singletonList(pictureHd)), 65);
        assertEquals(inMemoryServiceImpl.calculateScore(chalet, Collections.singletonList(pictureHd)), 65);
        assertEquals(inMemoryServiceImpl.calculateScore(garage, Collections.singletonList(pictureHd)), 60);
    }

    @Test
    public void testFindAllAds() {
        AdVO flat = new AdVO(1, "FLAT", "Descripción", Collections.singletonList(1), 20, 0, 0, null);
        AdVO chalet = new AdVO(2, "CHALET", "Descripción", Collections.singletonList(1), 20, 50, 0, null);
        AdVO garage = new AdVO(2, "GARAGE", null, Collections.singletonList(1), 0, 0, 0, null);
        List<AdVO> adsToReturn = Arrays.asList(flat, chalet, garage);
        when(inMemoryRepository.findAllAds()).thenReturn(adsToReturn);

        List<AdVO> adsReturned = inMemoryServiceImpl.findAllAds();

        assertEquals(adsToReturn.size(), adsReturned.size());
        assertArrayEquals(adsToReturn.stream().map(AdVO::getId).toArray(), adsReturned.stream().map(AdVO::getId).toArray());
    }

    @Test
    public void testFindAllPictures() {
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");
        PictureVO pictureSd = new PictureVO(2, "http://www.idealista.com/pictures/2", "SD");
        List<PictureVO> picturesToReturn = Arrays.asList(pictureHd, pictureSd);
        when(inMemoryRepository.findAllPictures()).thenReturn(picturesToReturn);

        List<PictureVO> picturesReturned = inMemoryServiceImpl.findAllPictures();

        assertEquals(picturesToReturn.size(), picturesReturned.size());
        assertArrayEquals(picturesToReturn.stream().map(PictureVO::getId).toArray(), picturesReturned.stream().map(PictureVO::getId).toArray());
    }

    @Test
    public void testFindPicturesByAd() {
        AdVO adWithHdAndSDPictures = new AdVO(1, "", "", Arrays.asList(1, 2), 0, 0, 0, null);
        PictureVO pictureHd = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");
        PictureVO pictureSd = new PictureVO(2, "http://www.idealista.com/pictures/2", "SD");
        List<PictureVO> picturesToReturn = Arrays.asList(pictureHd, pictureSd);
        when(inMemoryRepository.findPicturesByAd(adWithHdAndSDPictures.getId())).thenReturn(picturesToReturn);

        List<PictureVO> picturesReturned = inMemoryServiceImpl.findPicturesByAd(1);

        assertEquals(picturesToReturn.size(), picturesReturned.size());
        assertEquals(picturesToReturn.size(), picturesReturned.size());
    }

    @Test
    public void testFillIrrelevantSince() {
        AdVO relevantAd = new AdVO(1, "", "", Arrays.asList(1, 2), 0, 0, 40, null);
        AdVO irrelevantAd = new AdVO(2, "", "", Arrays.asList(1, 2), 0, 0, 39, null);

        inMemoryServiceImpl.fillIrrelevantSince(relevantAd);
        inMemoryServiceImpl.fillIrrelevantSince(irrelevantAd);

        assertNull(relevantAd.getIrrelevantSince());
        assertNotNull(irrelevantAd.getIrrelevantSince());
    }

    @Test
    public void testCreateQualityAdFromAdVO() {
        Date irrelevantSince = new Date(System.currentTimeMillis());
        AdVO ad = new AdVO(1, "", "", Arrays.asList(1, 2), 0, 0, 39, irrelevantSince);
        PictureVO picture = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");
        List<AdVO> ads = Collections.singletonList(ad);
        List<PictureVO> pictures = Collections.singletonList(picture);
        when(inMemoryRepository.findAllAds()).thenReturn(ads);
        when(inMemoryRepository.findPicturesByAd(ad.getId())).thenReturn(pictures);

        List<QualityAd> qualityAds = inMemoryServiceImpl.qualityListing();

        for (int i = 0; i < qualityAds.size(); i++) {
            AdVO adToCompare = ads.get(i);
            QualityAd qualityAd = qualityAds.get(i);

            assertEquals(adToCompare.getId(), qualityAd.getId());
            assertEquals(adToCompare.getIrrelevantSince(), qualityAd.getIrrelevantSince());
            assertEquals(adToCompare.getDescription(), qualityAd.getDescription());
            assertEquals(adToCompare.getTypology(), qualityAd.getTypology());
            assertEquals(adToCompare.getHouseSize(), qualityAd.getHouseSize());
            assertEquals(adToCompare.getGardenSize(), qualityAd.getGardenSize());
            assertEquals(adToCompare.getScore(), qualityAd.getScore());
            assertEquals(pictures.stream().map(PictureVO::getUrl).collect(Collectors.toList()), qualityAd.getPictureUrls());
        }
    }

    @Test
    public void testCreatePublicAdFromAdVO() {
        Date irrelevantSince = new Date(System.currentTimeMillis());
        AdVO ad = new AdVO(1, "", "", Arrays.asList(1, 2), 0, 0, 39, irrelevantSince);
        PictureVO picture = new PictureVO(1, "http://www.idealista.com/pictures/2", "HD");
        List<AdVO> ads = Collections.singletonList(ad);
        List<PictureVO> pictures = Collections.singletonList(picture);
        when(inMemoryRepository.findAllAds()).thenReturn(ads);
        when(inMemoryRepository.findPicturesByAd(ad.getId())).thenReturn(pictures);

        List<PublicAd> publicAds = inMemoryServiceImpl.publicListing();

        for (int i = 0; i < publicAds.size(); i++) {
            AdVO adToCompare = ads.get(i);
            PublicAd publicAd = publicAds.get(i);

            assertEquals(adToCompare.getId(), publicAd.getId());
            assertEquals(adToCompare.getDescription(), publicAd.getDescription());
            assertEquals(adToCompare.getTypology(), publicAd.getTypology());
            assertEquals(adToCompare.getHouseSize(), publicAd.getHouseSize());
            assertEquals(adToCompare.getGardenSize(), publicAd.getGardenSize());
            assertEquals(pictures.stream().map(PictureVO::getUrl).collect(Collectors.toList()), publicAd.getPictureUrls());
        }
    }


}
