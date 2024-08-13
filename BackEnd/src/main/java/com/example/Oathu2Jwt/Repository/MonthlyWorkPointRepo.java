package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.MonthlyWorkPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MonthlyWorkPointRepo extends JpaRepository<MonthlyWorkPoint,Long> {
    public List<MonthlyWorkPoint> findByUserInfoId(Long id);
    public List<MonthlyWorkPoint> findByUserInfoEmailId(String id);

}
