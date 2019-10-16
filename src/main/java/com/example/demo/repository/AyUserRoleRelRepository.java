package com.example.demo.repository;

import com.example.demo.model.AyUserRoleRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AyUserRoleRelRepository extends JpaRepository<AyUserRoleRel, String> {
    List<AyUserRoleRel> findByUserId(@Param("userId")String userID);
}
