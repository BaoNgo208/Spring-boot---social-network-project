package com.example.Oathu2Jwt.Model.MongoDBEntity.Notification;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter

public class LikeNotification extends PostRelatedNotification {

    public LikeNotification(Long senderId, String senderName, Long receiverId, Date  createdAt, NotificationType type, Long postId) {
        super(senderId, senderName, receiverId, createdAt,type, postId);
    }
}