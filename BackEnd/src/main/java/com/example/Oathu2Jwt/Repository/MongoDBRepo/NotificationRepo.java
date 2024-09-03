package com.example.Oathu2Jwt.Repository.MongoDBRepo;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface NotificationRepo extends MongoRepository<Notification,String> {
        List<Notification> findByCreatedAtBefore(Date date);
        Page<Notification> findByReceiverId(Long receiverId, Pageable pageable);
}
