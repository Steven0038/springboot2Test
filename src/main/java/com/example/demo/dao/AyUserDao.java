package com.example.demo.dao;


import com.example.demo.model.AyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AyUserDao {
    /**
     * 通過用戶名和密碼查詢用戶
     * @param name
     * @param password
     * @return
     */
    AyUser findByNameAndPassword(@Param("name") String name,
                                 @Param("password") String password);
}
