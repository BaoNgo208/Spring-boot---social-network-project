package com.example.Oathu2Jwt.Model.Entity.Notification;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LikeNotification extends PostRelatedNotification {

    public LikeNotification(Long senderId, String senderName, Long receiverId, NotificationType type, Long postId) {
        super(senderId, senderName, receiverId, type, postId);
    }
}