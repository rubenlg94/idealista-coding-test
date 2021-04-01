package com.idealista.services;

import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;

import java.util.List;

public interface InMemoryService {

    List<AdVO> findAllAds();

    List<PictureVO> findAllPictures();

    List<PictureVO> findPicturesByAd(int adId);

    int calculateScore(AdVO ad, List<PictureVO> pictures);

}
