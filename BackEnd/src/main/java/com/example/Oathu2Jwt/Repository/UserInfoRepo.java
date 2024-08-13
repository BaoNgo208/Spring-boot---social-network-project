package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.EmployeeEntity;
import com.example.Oathu2Jwt.Model.Entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepo extends JpaRepository<UserInfoEntity,Long> {
    Optional<UserInfoEntity> findByEmailId(String emailId);
    Optional<UserInfoEntity> findByAccName(String accName);

    public UserInfoEntity findByEmployee(EmployeeEntity employee);
    public List<UserInfoEntity> findByEmployeeUserName(String username);

}
