package com.example.demo.service;

import com.example.demo.model.AyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface AyUserService {
    Optional<AyUser> findById(String id);

    List<AyUser> findAll();

    // 異步查詢
    Future<List<AyUser>> findAsynAll();

    List<AyUser> findByName(String name);

    List<AyUser> findByNameLike(String name);

    List<AyUser> findByIdIn(Collection<String> ids);

    //mybatis
    AyUser findByNameAndPassword(String name, String password);

    AyUser findByNameAndPasswordRetry(String name, String password);

    AyUser save(AyUser ayUser);

    void delete(String id);

    //分頁
    Page<AyUser> findAll(Pageable pageable);
}
