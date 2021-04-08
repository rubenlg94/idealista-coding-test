package com.idealista.utilities;

import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utilities {

    public static int[] getManuallyCalculatedScores() {
        int[] scores = new int[8];
        scores[0] = (Math.max(-10 + 5, 0)); // Sin imágenes, con descripción
        scores[1] = (Math.max(20 + 5 + 15 + 40, 0)); // Con una imagen HD, con descripción, con 3 palabras destacadas, completo
        scores[2] = (Math.max(20, 0)); // Con una imagen HD
        scores[3] = (Math.max(10 + 5 + 25 + 40, 0)); // Con una imagen SD, con descripción, con 5 palabras destacadas, completo
        scores[4] = (Math.max(30 + 5, 0)); // Con una imagen SD y una HD, con descripción
        scores[5] = (Math.max(10 + 40, 0)); // Con una imagen SD, completo
        scores[6] = (Math.max(-10 + 5, 0)); // Sin imágenes, con descripción
        scores[7] = (Math.max(20 + 5 + 10, 0)); // Con dos imágenes SD, con descripción, descripción entre 20 y 49 palabras
        return scores;
    }

    public static List<AdVO> generateTestAds() {
        List<AdVO> ads = new ArrayList<>();
        ads.add(new AdVO(1, "CHALET", "Este piso es una ganga, compra, compra, COMPRA!!!!!", Collections.emptyList(), 300, null, null, null));
        ads.add(new AdVO(2, "FLAT", "Nuevo ático céntrico recién reformado. No deje pasar la oportunidad y adquiera este ático de lujo", Arrays.asList(4), 300, null, null, null));
        ads.add(new AdVO(3, "CHALET", "", Arrays.asList(2), 300, null, null, null));
        ads.add(new AdVO(4, "FLAT", "Ático céntrico muy luminoso y recién reformado, parece nuevo", Arrays.asList(5), 300, null, null, null));
        ads.add(new AdVO(5, "FLAT", "Pisazo,", Arrays.asList(3, 8), 300, null, null, null));
        ads.add(new AdVO(6, "GARAGE", "", Arrays.asList(6), 300, null, null, null));
        ads.add(new AdVO(7, "GARAGE", "Garaje en el centro de Albacete", Collections.emptyList(), 300, null, null, null));
        ads.add(new AdVO(8, "CHALET", "Maravilloso chalet situado en lAs afueras de un pequeño pueblo rural. El entorno es espectacular, las vistas magníficas. ¡Cómprelo ahora!", Arrays.asList(1, 7), 300, null, null, null));
        return ads;
    }

    public static List<PictureVO> generateTestPictures() {
        List<PictureVO> pictures = new ArrayList<>();
        pictures.add(new PictureVO(1, "http://www.idealista.com/pictures/1", "SD"));
        pictures.add(new PictureVO(2, "http://www.idealista.com/pictures/2", "HD"));
        pictures.add(new PictureVO(3, "http://www.idealista.com/pictures/3", "SD"));
        pictures.add(new PictureVO(4, "http://www.idealista.com/pictures/4", "HD"));
        pictures.add(new PictureVO(5, "http://www.idealista.com/pictures/5", "SD"));
        pictures.add(new PictureVO(6, "http://www.idealista.com/pictures/6", "SD"));
        pictures.add(new PictureVO(7, "http://www.idealista.com/pictures/7", "SD"));
        pictures.add(new PictureVO(8, "http://www.idealista.com/pictures/8", "HD"));
        return pictures;
    }

}
