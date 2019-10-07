package com.example.demo.service.impl;

import com.example.demo.model.AyUser;
import com.example.demo.repository.AyUserRepository;
import com.example.demo.service.AyUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * user service implement class
 */
@Transactional
@Service
public class AyUserServiceImpl implements AyUserService {

    @Resource(name = "ayUserRepository")
    private AyUserRepository ayUserRepository;

    @Resource
    private RedisTemplate redisTemplate;

    private static final String ALL_USER = "ALL_USER_LIST";

    @Override
    public Optional<AyUser> findById(String id) {
        // step.1 查詢 Redis 緩存的所有數據
        List<AyUser> ayUserList = redisTemplate.opsForList().range(ALL_USER, 0, -1);
        if (ayUserList != null && ayUserList.size() > 0) {
            for (AyUser ayUser : ayUserList) {
                if (ayUser.getId().equals(id)) {
                    return Optional.of(ayUser);
                }
            }
        }
        // step.2 如果 Redis 找不到就查詢數據庫中的數據
        Optional<AyUser> ayUsers = ayUserRepository.findById(id);
        if (ayUsers.isPresent()) {
            // step.3 將數據插入緩存中
            redisTemplate.opsForList().leftPush(ALL_USER, ayUsers.get());
        }
        return ayUsers;
    }

    @Override
    public List<AyUser> findAll() {
        return ayUserRepository.findAll();
    }

    @Override
    public List<AyUser> findByName(String name) {
        return ayUserRepository.findByName(name);
    }

    @Override
    public List<AyUser> findByNameLike(String name) {
        return ayUserRepository.findByNameLike(name);
    }

    @Override
    public List<AyUser> findByIdIn(Collection<String> ids) {
        return ayUserRepository.findByIdIn(ids);
    }

    @Override
    public Page<AyUser> findAll(Pageable pageable) {
        return ayUserRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public AyUser save(AyUser ayUser) {
        AyUser saveUser = ayUserRepository.save(ayUser);

        // null pointer exception, 測試 transaction rollback, 如果有 @Transacton 就會 rollback 不會 insert
//        String error = null;
//        error.split("/");

        return saveUser;
    }

    @Override
    public void delete(String id) {
        ayUserRepository.deleteById(id);
    }


}
