package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.Entity.MonthlyWorkPoint;
import com.example.Oathu2Jwt.Model.Entity.WorkPoint;
import com.example.Oathu2Jwt.Repository.MonthlyWorkPointRepo;
import com.example.Oathu2Jwt.Repository.WorkPointRepo;
import com.example.Oathu2Jwt.Service.WorkPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkPointServiceImpl implements WorkPointService {
    private final WorkPointRepo workPointRepo;
    private final MonthlyWorkPointRepo monthlyWorkPointRepo;
    @Override
    public WorkPoint createWorkPoint(WorkPoint workPoint) {
        return workPointRepo.save(workPoint);
    }

    @Override
    public WorkPoint checkWorkPoint(String emailId) {
        List<MonthlyWorkPoint> list = monthlyWorkPointRepo.findByUserInfoEmailId(emailId);
        MonthlyWorkPoint monthlyWorkPoint = list.get(list.size() - 1 ) ;

        if( workPointRepo.findByDateAndMonthlyCheckInUserInfo(java.sql.Date.valueOf(LocalDate.now()),
                monthlyWorkPoint.getUserInfo()
        ) == null  ) {

            WorkPoint newWorkPoint = new WorkPoint();
            newWorkPoint.setCheckInTime(LocalTime.now());
            newWorkPoint.setDate(java.sql.Date.valueOf(LocalDate.now()));
            newWorkPoint.setMonthlyCheckIn(monthlyWorkPoint);
            newWorkPoint.setLate(false);



            LocalTime currentTime = LocalTime.now();
            LocalTime nineAM = LocalTime.of(9, 0);
            int comparison = currentTime.compareTo(nineAM);
            if(comparison > 0) {
                newWorkPoint.setLate(true);
                monthlyWorkPoint.setLateDays(monthlyWorkPoint.getLateDays() + 1);
            }

            monthlyWorkPoint.getWorkPoints().add(newWorkPoint);
            monthlyWorkPoint.setNumOfCheckIn(monthlyWorkPoint.getNumOfCheckIn() + 1);

            monthlyWorkPointRepo.save(monthlyWorkPoint);
            return newWorkPoint;
        }
        else {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED,"have already check in today");
        }
    }
}
