package com.example.demo.repository;

import com.example.demo.model.AyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface AyUserRepository extends JpaRepository<AyUser, String> {
//public interface AyUserRepository extends CrudRepository<AyUser, String> {
    // 自定義查詢方法
    List<AyUser> findByName(String name);

    List<AyUser> findByNameLike(String name);

    List<AyUser> findByIdIn(Collection<String> ids);

}



