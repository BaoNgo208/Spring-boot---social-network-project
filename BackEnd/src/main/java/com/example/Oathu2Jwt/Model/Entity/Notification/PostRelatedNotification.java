package com.example.Oathu2Jwt.Model.Entity.Notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class PostRelatedNotification extends Notification {
    private Long postId;

    public PostRelatedNotification(Long senderId, String senderName, Long receiverId, NotificationType type, Long postId) {
        super(senderId, senderName, receiverId, type);
        this.postId = postId;
    }
}