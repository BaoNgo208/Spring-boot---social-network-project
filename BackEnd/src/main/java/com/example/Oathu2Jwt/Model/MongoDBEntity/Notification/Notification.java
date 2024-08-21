package com.example.Oathu2Jwt.Model.MongoDBEntity.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long senderId ;
    private String senderName;
    private Long receiverId;
    private NotificationType type;
}
