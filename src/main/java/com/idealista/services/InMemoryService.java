package com.idealista.services;

import com.idealista.valueobjects.AdVO;
import com.idealista.valueobjects.PictureVO;

import java.util.List;

public interface InMemoryService {

    int calculateScore(AdVO ad, List<PictureVO> pictures);

}
