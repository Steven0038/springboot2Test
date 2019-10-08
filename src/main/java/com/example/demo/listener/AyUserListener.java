package com.example.demo.listener;

import com.example.demo.model.AyUser;
import com.example.demo.service.AyUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

@WebListener
public class AyUserListener implements ServletContextListener {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AyUserService ayUserService;

    private static final String ALL_USER = "ALL_USER_LIST";

    Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 查詢數據庫所有的用戶
        List<AyUser> ayUsers = ayUserService.findAll();
        // 清除 Redis 緩存中的用戶數據
        redisTemplate.delete(ALL_USER);
        // 將數據放到 Redis 緩存中
        redisTemplate.opsForList().leftPushAll(ALL_USER, ayUsers);
        // 在真實項目需要註解掉, 查詢所有的用戶數據
        List<AyUser> queryUserList = redisTemplate.opsForList().range(ALL_USER, 0, -1);
//        System.out.println("緩存中的用戶數有: " + queryUserList.size());
//        System.out.println("ServletContext 初始化");
        logger.info("ServletContext 初始化");
        logger.info("緩存中的用戶數有: " + queryUserList.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        System.out.println("ServletContext 上下文銷毀");
        logger.info("ServletContext 上下文銷毀");
    }


}
