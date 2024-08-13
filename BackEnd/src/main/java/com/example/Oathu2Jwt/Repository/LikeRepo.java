package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepo extends JpaRepository<LikeEntity,Long> {
    LikeEntity findByPostIdAndUserInfoId(Long postId, Long userInfoId);
}
