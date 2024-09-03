package com.example.Oathu2Jwt.Model.MongoDBEntity.Notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public abstract class PostRelatedNotification extends Notification {

    public PostRelatedNotification(Long senderId, String senderName, Long receiverId, Date createdAt, NotificationType type, Long postId) {
        super(null,senderId, senderName, receiverId, createdAt,type,postId);
    }
}