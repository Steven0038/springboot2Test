package com.example.demo.mail.impl;

import com.example.demo.mail.SendJunkMailService;
import com.example.demo.model.AyUser;
import com.example.demo.service.AyUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class SendJunkMailServiceImpl implements SendJunkMailService {

    @Autowired
    JavaMailSender mailSender;
    @Resource
    private AyUserService ayUserService;

    @Value("${spring.mail.username}")
    private String from;

    public static final Logger logger = LogManager.getLogger(SendJunkMailServiceImpl.class);

    @Override
    public boolean sendJunkMail(List<AyUser> ayUsers) {
        if (ayUsers == null || ayUsers.size() <= 0) {
            return Boolean.FALSE;
        }

        try {
            for (AyUser ayUser : ayUsers) {
                MimeMessage mimeMessage = this.mailSender.createMimeMessage();
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                // 郵件發送方
                message.setFrom(from);
                // 郵件主題
                message.setSubject("西瓜今日特賣");
                message.setTo("al_test@163.com");
                // 郵件內容
                message.setText(ayUser.getName() + ", 你知道嗎? 今日西瓜特賣 一斤 9 元");
                // 發送郵件
                this.mailSender.send(mimeMessage);
            }
        } catch (Exception ex) {
            logger.error("SendJunkMail error and ayUser=%s", ayUsers, ex);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
