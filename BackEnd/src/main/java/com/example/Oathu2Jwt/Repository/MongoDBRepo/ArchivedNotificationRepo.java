package com.example.Oathu2Jwt.Repository.MongoDBRepo;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Notification.ArchivedNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArchivedNotificationRepo extends MongoRepository<ArchivedNotification,String> {

    Page<ArchivedNotification> findByReceiverId(Long receiverId, Pageable pageable);
}
