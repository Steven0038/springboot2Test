package com.example.demo.mail;

import com.example.demo.model.AyUser;

import java.util.List;

public interface SendJunkMailService {
    boolean sendJunkMail(List<AyUser> ayUsers);
}
