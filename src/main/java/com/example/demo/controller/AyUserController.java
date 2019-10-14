package com.example.demo.controller;

import com.example.demo.error.BusinessException;
import com.example.demo.model.AyUser;
import com.example.demo.service.AyUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/ayUser")
public class AyUserController {
    @Resource
    private AyUserService ayUserService;

    @RequestMapping("/test")
    public String test(Model model) {
        // 查詢數據庫所有用戶
        List<AyUser> ayUsers = ayUserService.findAll();
        model.addAttribute("users", ayUsers);
        return "ayUser";
    }

    @RequestMapping("/findAll")
    public String findAll(Model model) {
        List<AyUser> ayUsers = ayUserService.findAll();
        model.addAttribute("users", ayUsers);
        throw new BusinessException("業務異常");
    }

    @RequestMapping("/findByNameAndPassword")
    public String findByNameAndPassword(Model model) {
        AyUser ayUser = ayUserService.findByNameAndPasswordRetry("steven", "123456");
        model.addAttribute("users", ayUser);
        return "Success";
    }
}
