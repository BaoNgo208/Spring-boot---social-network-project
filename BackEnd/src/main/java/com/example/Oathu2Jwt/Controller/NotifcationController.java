package com.example.Oathu2Jwt.Controller;


import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.Notification;
import com.example.Oathu2Jwt.Service.EmployeeService;
import com.example.Oathu2Jwt.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotifcationController {
    private final NotificationService notificationService;
    private final EmployeeService employeeService;
    @GetMapping("/get/notification")
    public ResponseEntity<Page<Notification>> getNotification(
            Principal principal,
            @RequestParam int page,
            @RequestParam int size) {
        UserInfoEntity user = employeeService.getUserByEmail(principal.getName());
        Page<Notification> notifications = notificationService.getNotificationOfUser(user.getId().toString(),page,size);
        return  ResponseEntity.ok(notifications);
    }

}

