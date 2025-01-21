package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.User.UserEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepo extends JpaRepository<UserInfoEntity,Long> {
    Optional<UserInfoEntity> findByEmailId(String emailId);
    Optional<UserInfoEntity> findByAccName(String accName);

    public UserInfoEntity findByEmployee(UserEntity employee);
    public List<UserInfoEntity> findByEmployeeUserName(String username);


    Page<UserInfoEntity> findByEmployeeUserName(String username, Pageable pageable);

    Page<UserInfoEntity> findByEmployeeUserNameContaining(String username, Pageable pageable);

    List<UserInfoEntity> findByEmployeeUserNameContaining(String username);




}
