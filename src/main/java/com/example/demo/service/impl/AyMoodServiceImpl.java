package com.example.demo.service.impl;

import com.example.demo.model.AyMood;
import com.example.demo.producer.AyMoodProducer;
import com.example.demo.repository.AyMoodRepository;
import com.example.demo.service.AyMoodService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;

@Service
public class AyMoodServiceImpl implements AyMoodService {
    @Resource
    AyMoodRepository ayMoodRepository;

    @Override
    public AyMood save(AyMood ayMood) {
        return ayMoodRepository.save(ayMood);
    }

    // 隊列
    private static Destination destination = new ActiveMQQueue("ay.queue.asyn.save");

    @Resource
    private AyMoodProducer ayMoodProducer;

    @Override
    public String asySave(AyMood ayMood) {
        // 往隊列 ay.queue.asyn.save 推送消息, 內容為說說實體
        ayMoodProducer.sendMessage(destination, ayMood);
        return "success";
    }
}
