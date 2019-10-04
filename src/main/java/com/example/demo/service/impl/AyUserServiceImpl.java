package com.example.demo.service.impl;

import com.example.demo.model.AyUser;
import com.example.demo.repository.AyUserRepository;
import com.example.demo.service.AyUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Optional<AyUser> findById(String id) {
        return ayUserRepository.findById(id);
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
