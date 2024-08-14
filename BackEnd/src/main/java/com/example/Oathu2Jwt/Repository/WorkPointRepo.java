package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Model.Entity.WorkPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface WorkPointRepo extends JpaRepository<WorkPoint,Long> {
    public WorkPoint findByDate(Date date);
    public WorkPoint findByDateAndMonthlyCheckInUserInfo(Date date, UserInfoEntity userInfo);
}
