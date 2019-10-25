package com.example.demo.service.impl;

import com.example.demo.error.BusinessException;
import com.example.demo.model.AyRole;
import com.example.demo.model.AyUser;
import com.example.demo.model.AyUserRoleRel;
import com.example.demo.service.AyRoleService;
import com.example.demo.service.AyUserRoleRelService;
import com.example.demo.service.AyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//@Service
public class CustomUserService implements UserDetailsService {
    @Resource
    private AyUserService ayUserService;

    @Resource
    private AyUserRoleRelService ayUserRoleRelService;

    @Resource
    private AyRoleService ayRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        AyUser ayuser = ayUserService.findByUserName(name);
        if (ayuser == null) {
            throw new BusinessException("用戶不存在");
        }
        // 獲取用戶的所有關聯角色
        List<AyUserRoleRel> ayUserRoleRels = ayUserRoleRelService.findByUserId(ayuser.getId());
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (ayUserRoleRels != null && ayUserRoleRels.size() > 0) {
            for (AyUserRoleRel ayUserRoleRel : ayUserRoleRels) {
                // 獲取用戶角色名稱
                String roleName = ayRoleService.find(ayUserRoleRel.getRoleId()).getName();
                authorities.add(new SimpleGrantedAuthority(roleName));
            }
        }
        return new User(ayuser.getName(), passwordEncoder.encode(ayuser.getPassword()), authorities);
    }
}
