package com.example.demo;

import com.example.demo.model.AyUser;
import com.example.demo.service.AyUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySpringBootApplicationTests {
    @Resource
    private JdbcTemplate jdbcTemplate;


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


}
