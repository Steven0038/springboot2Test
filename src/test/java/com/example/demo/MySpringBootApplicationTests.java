package com.example.demo;

import com.example.demo.dao.AyUserDao;
import com.example.demo.model.AyMood;
import com.example.demo.model.AyUser;
import com.example.demo.producer.AyMoodProducer;
import com.example.demo.service.AyMoodService;
import com.example.demo.service.AyUserService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.jms.Destination;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySpringBootApplicationTests {
    @Resource
    private JdbcTemplate jdbcTemplate;

    Logger logger = LogManager.getLogger(this.getClass());

    @Test
    public void contextLoads() {
    }

    @Test
    public void mySqlTest() {
        String sql = "select id, name, password from ay_user";
        List<AyUser> userList = jdbcTemplate.query(sql, new RowMapper<AyUser>() {
            @Override
            public AyUser mapRow(ResultSet resultSet, int i) throws SQLException {
                AyUser user = new AyUser();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        });
        System.out.println("查詢成功");
        for (AyUser user : userList) {
            System.out.println("[id]:" + user.getId() + ", [name]:" + user.getName());
        }
    }

    @Resource
    private AyUserService ayUserService;

    @Test
    public void testRepository() {
        // 查詢所有數據
        List<AyUser> userList = ayUserService.findAll();
        System.out.println("findAll():" + userList.size());
        // 通過 name 查詢數據
        List<AyUser> userList2 = ayUserService.findByName("steven");
        System.out.println("findByName():" + userList2.size());
        Assert.isTrue(userList2.get(0).getName().equals("steven"), "data error!");
        // 通過 name 模糊查詢數據
        List<AyUser> userList3 = ayUserService.findByNameLike("%ste%");
        System.out.println("findByNameLike():" + userList3.size());
        Assert.isTrue(userList3.get(0).getName().equals("steven"), "data error!");
        // 通過 id 列表查詢數據
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        List<AyUser> userList4 = ayUserService.findByIdIn(ids);
        System.out.println("findByIdIn()" + userList4.size());
        // 分頁查詢數據
        PageRequest pageRequest = new PageRequest(0, 10);
        Page<AyUser> userList5 = ayUserService.findAll(pageRequest);
        System.out.println("page findAll():" + userList5.getTotalPages() + "/" + userList5.getSize());
        // 新增數據
        AyUser ayUser = new AyUser();
        ayUser.setId("3");
        ayUser.setName("test");
        ayUser.setPassword("123");
        ayUserService.save(ayUser);
        // 刪除數據
        ayUserService.delete("3");
    }

    @Test
    public void testTransaction() {
        AyUser ayUser = new AyUser();
        ayUser.setId("3");
        ayUser.setName("阿華");
        ayUser.setPassword("123");
        ayUserService.save(ayUser);
    }

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        // 增加 Key:name, value:ay
        redisTemplate.opsForValue().set("name", "ay");
        String name = (String) redisTemplate.opsForValue().get("name");
        System.out.println(name);
        // 刪除
        redisTemplate.delete("name");
        // 更新
        redisTemplate.opsForValue().set("name", "al");
        // 查詢
        name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("name");
    }

    @Test
    public void testFindById() {
        Long redisUserSize = 0L;
        // 查詢 id = 1 的數據, 該數據存在於 Redis 緩存中
        Optional<AyUser> ayUser = ayUserService.findById("1");
        redisUserSize = redisTemplate.opsForList().size("ALL_USER_LIST");
        System.out.println("目前緩存中的用戶數量: " + redisUserSize);
        System.out.println("--->>> id: " + ayUser.get().getId() + " name: " + ayUser.get().getName());
        // 查詢 id = 2 的數據, 該數據存在於 Redis 緩存中
        Optional<AyUser> ayUser1 = ayUserService.findById("2");
        redisUserSize = redisTemplate.opsForList().size("ALL_USER_LIST");
        System.out.println("目前緩存中的用戶數量: " + redisUserSize);
        System.out.println("--->>> id: " + ayUser1.get().getId() + " name: " + ayUser1.get().getName());
        // 查詢 id = 4 的用戶, 該數據僅存於資料庫中
        // 所以會從 DB 更新到緩存中
        Optional<AyUser> ayUser3 = ayUserService.findById("4");
        redisUserSize = redisTemplate.opsForList().size("ALL_USER_LIST");
        System.out.println("目前緩存中的用戶數量: " + redisUserSize);
        System.out.println("--->>> id: " + ayUser3.get().getId() + " name: " + ayUser3.get().getName());
    }

    @Test
    public void testLog4j() {
        ayUserService.delete("4");
        logger.info("delete success!!!");
    }

    @Test
    public void testMybatis() {
        AyUser ayUser = ayUserService.findByNameAndPassword("steven", "12456");
        logger.info("testMybatis-->" + ayUser.getId() + ", " + ayUser.getName());
    }

    @Resource
    AyMoodService ayMoodService;

    @Test
    public void testAyMood() {
        AyMood ayMood = new AyMood();
        ayMood.setId("1");
        ayMood.setUserId("1");
        ayMood.setPraiseNum(0);
        ayMood.setContent("這是第一條微信說說");
        ayMood.setPublishTime(new Date());
        ayMoodService.save(ayMood);
    }

    @Resource
    private AyMoodProducer ayMoodProducer;

    @Test
    public void testActiveMQ() {
        Destination destination = new ActiveMQQueue("ay.queue");
        // 基本隊列
        ayMoodProducer.sendMessage(destination, "hello,mq!!!!");
    }

    @Test
    public void testActiveMQAsynSave() {
        AyMood ayMood = new AyMood();
        ayMood.setId("2");
        ayMood.setUserId("2");
        ayMood.setPraiseNum(0);
        ayMood.setContent("這是我的第一條威信說說!!!");
        ayMood.setPublishTime(new Date());
        // 異步消費模式
        String msg = ayMoodService.asySave(ayMood);
        System.out.println("異步發表說說 : " + msg);
    }

    @Test
    public void testAsync() {
        Long startTime = System.currentTimeMillis();
        System.out.println("第一次查詢所有用戶!");
        List<AyUser> ayUsers = ayUserService.findAll();
        System.out.println("第二次查詢所有用戶!");
        List<AyUser> ayUsers2 = ayUserService.findAll();
        System.out.println("第三次查詢所有用戶!");
        List<AyUser> ayUsers3 = ayUserService.findAll();
        Long endTime = System.currentTimeMillis();
        System.out.println("總共消耗" + (endTime - startTime) + "毫秒");
    }

    @Test
    public void testAsync2() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("第一次查詢所有用戶!");
        Future<List<AyUser>> ayUsers = ayUserService.findAsynAll();
        System.out.println("第二次查詢所有用戶!");
        Future<List<AyUser>> ayUsers2 = ayUserService.findAsynAll();
        System.out.println("第三次查詢所有用戶!");
        Future<List<AyUser>> ayUsers3 = ayUserService.findAsynAll();
        while (true){
            if (ayUsers.isDone() && ayUsers2.isDone() && ayUsers3.isDone()){
                break;
            } else {
                Thread.sleep(10);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("總共消耗" + (endTime - startTime) + "毫秒");
    }

}
