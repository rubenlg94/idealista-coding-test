package com.idealista.repositores;

import com.idealista.constants.Values;
import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryRepository {

    private final List<AdVO> ads;
    private final List<PictureVO> pictures;

    public InMemoryRepository() {
        ads = new ArrayList<AdVO>();
        ads.add(new AdVO(1, "CHALET", "Este piso es una ganga, compra, compra, COMPRA!!!!!", Collections.emptyList(), 300, null, null, null));
        ads.add(new AdVO(2, "FLAT", "Nuevo ático céntrico recién reformado. No deje pasar la oportunidad y adquiera este ático de lujo", Arrays.asList(4), 300, null, null, null));
        ads.add(new AdVO(3, "CHALET", "", Arrays.asList(2), 300, null, null, null));
        ads.add(new AdVO(4, "FLAT", "Ático céntrico muy luminoso y recién reformado, parece nuevo", Arrays.asList(5), 300, null, null, null));
        ads.add(new AdVO(5, "FLAT", "Pisazo,", Arrays.asList(3, 8), 300, null, null, null));
        ads.add(new AdVO(6, "GARAGE", "", Arrays.asList(6), 300, null, null, null));
        ads.add(new AdVO(7, "GARAGE", "Garaje en el centro de Albacete", Collections.emptyList(), 300, null, null, null));
        ads.add(new AdVO(8, "CHALET", "Maravilloso chalet situado en lAs afueras de un pequeño pueblo rural. El entorno es espectacular, las vistas magníficas. ¡Cómprelo ahora!", Arrays.asList(1, 7), 300, null, null, null));

        pictures = new ArrayList<PictureVO>();
        pictures.add(new PictureVO(1, "http://www.idealista.com/pictures/1", "SD"));
        pictures.add(new PictureVO(2, "http://www.idealista.com/pictures/2", "HD"));
        pictures.add(new PictureVO(3, "http://www.idealista.com/pictures/3", "SD"));
        pictures.add(new PictureVO(4, "http://www.idealista.com/pictures/4", "HD"));
        pictures.add(new PictureVO(5, "http://www.idealista.com/pictures/5", "SD"));
        pictures.add(new PictureVO(6, "http://www.idealista.com/pictures/6", "SD"));
        pictures.add(new PictureVO(7, "http://www.idealista.com/pictures/7", "SD"));
        pictures.add(new PictureVO(8, "http://www.idealista.com/pictures/8", "HD"));
    }

    public List<AdVO> findAllAds() {
        return ads;
    }

    public List<PictureVO> findAllPictures() {
        return pictures;
    }

    public List<PictureVO> findPicturesByAd(int adId) {
        List<PictureVO> pictureVOS = new ArrayList<>();
        Optional<AdVO> optionalAdVO = ads.stream().filter(adVO -> adVO.getId() == adId).findAny();
        if (optionalAdVO.isPresent()) {
            AdVO ad = optionalAdVO.get();
            pictureVOS = pictures.stream().filter(pictureVO -> ad.getPictures().contains(pictureVO.getId())).collect(Collectors.toList());
        }
        return pictureVOS;
    }

    public List<AdVO> findRelevantAds() {
        return ads.stream().filter(adVO -> adVO.getScore() >= Values.Ads.MINIMUM_SCORE).sorted(Comparator.comparingInt(AdVO::getScore).reversed()).collect(Collectors.toList());
    }


}
