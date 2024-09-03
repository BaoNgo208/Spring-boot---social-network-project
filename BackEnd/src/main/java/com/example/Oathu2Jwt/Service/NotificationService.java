package com.example.Oathu2Jwt.Service;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.Notification;
import org.springframework.data.domain.Page;

public interface NotificationService {
    public void saveNotification(Notification notification);
    public void archiveOldNotification();
    public Page<Notification> getNotificationOfUser(String userId,int page,int size);
}
