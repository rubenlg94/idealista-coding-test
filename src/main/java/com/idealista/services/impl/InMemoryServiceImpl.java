package com.idealista.services.impl;

import com.idealista.services.InMemoryService;
import com.idealista.valueobjects.AdVO;
import org.springframework.stereotype.Service;

@Service
public class InMemoryServiceImpl implements InMemoryService {

    @Override
    public int calculateScore(AdVO ad) {
        int score = 0;
        score = Math.max(score, 0);
        score = Math.min(score, 100);
        return score;
    }

}
