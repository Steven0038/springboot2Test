package com.example.demo.service;

import com.example.demo.model.AyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AyUserService {
    Optional<AyUser> findById(String id);

    List<AyUser> findAll();

    List<AyUser> findByName(String name);

    List<AyUser> findByNameLike(String name);

    List<AyUser> findByIdIn(Collection<String> ids);

    AyUser save(AyUser ayUser);
    void delete(String id);
    //分頁
    Page<AyUser> findAll(Pageable pageable);
}
