package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.Entity.MonthlyWorkPoint;
import com.example.Oathu2Jwt.Model.Entity.UserInfoEntity;
import com.example.Oathu2Jwt.Repository.MonthlyWorkPointRepo;
import com.example.Oathu2Jwt.Repository.UserInfoRepo;
import com.example.Oathu2Jwt.Service.MonthlyWorkPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyWorkPointServiceImpl implements MonthlyWorkPointService {
    private final MonthlyWorkPointRepo monthlyWorkPointRepo;
    private final UserInfoRepo userInfoRepo;
    @Override
    public MonthlyWorkPoint createMonthlyWorkPoint(MonthlyWorkPoint monthlyWorkPoint) {
        return monthlyWorkPointRepo.save(monthlyWorkPoint);
    }
    public int compareMonthYear(Date databaseMonth) {
        YearMonth currentYearMonth = YearMonth.now();

        YearMonth database = YearMonth.from(databaseMonth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return currentYearMonth.compareTo(database);
    }
//    @EventListener(ApplicationReadyEvent.class)
//    public void autoCreateMonthlyWorkPoint() throws ParseException {
//        List<UserInfoEntity> userList = userInfoRepo.findAll();
//
//        for (UserInfoEntity user : userList) {
//            List<MonthlyWorkPoint> list = monthlyWorkPointRepo.findByUserInfoId(user.getId());
//
//            MonthlyWorkPoint monthlyWorkPoint = list.get(list.size() - 1);
//            if (monthlyWorkPoint == null || compareMonthYear(monthlyWorkPoint.getMonth()) > 0) {
//                MonthlyWorkPoint newMonth = new MonthlyWorkPoint();
//                newMonth.setLateDays(0);
//                newMonth.setNumOfCheckIn(0);
//                newMonth.setUserInfo(user);
//                newMonth.setWorkPoints(null);
//                YearMonth currentYearMonth = YearMonth.now();
//                String yearMonthString = currentYearMonth.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
//                Date date = new SimpleDateFormat("yyyy-MM").parse(yearMonthString);
//                newMonth.setMonth(date);
//                monthlyWorkPointRepo.save(newMonth);
//            }
//        }
//    }
}
