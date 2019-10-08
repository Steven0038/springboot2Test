package com.example.demo.quartz;

import com.example.demo.mail.SendJunkMailService;
import com.example.demo.model.AyUser;
import com.example.demo.service.AyUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定時器類
 */
@Component
@Configurable
@EnableScheduling
public class SendMailQuartz {

    // 日誌對象
    private static final Logger logger = LogManager.getLogger(SendMailQuartz.class);

    @Resource
    private SendJunkMailService sendJunkMailService;
    @Resource
    private AyUserService ayUserService;

    // 每 5 秒執行一次
    @Scheduled(cron = "*/5 * *  * * *")
    public void reportCurrentByCron() {
        List<AyUser> ayUsers =  ayUserService.findAll();
        if (ayUsers == null || ayUsers.size() <= 0){return;}
        // 發送郵件
        sendJunkMailService.sendJunkMail(ayUsers);
        logger.info("定時器運行了!!!");
    }
}
