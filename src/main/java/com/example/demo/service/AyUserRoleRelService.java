package com.example.demo.service;

import com.example.demo.model.AyUserRoleRel;

import java.util.List;

public interface AyUserRoleRelService {
    List<AyUserRoleRel> findByUserId(String userId);
}
