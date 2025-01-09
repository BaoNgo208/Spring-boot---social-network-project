package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.User.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity,Long> {
        public UserEntity findByUserName(String username);

        @Query("SELECT u.id FROM UserEntity u WHERE u.username = :username")
        List<UserEntity> findUsersByUserName(@Param("username") String username);

}
