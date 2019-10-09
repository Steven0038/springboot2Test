package com.example.demo.service;

import com.example.demo.model.AyMood;

public interface AyMoodService {
    AyMood save(AyMood ayMood);

    /**
     * 異步消費模式
     * @param ayMood
     * @return
     */
    String asySave(AyMood ayMood);
}
