package com.example.Oathu2Jwt.Model.MongoDBEntity.Notification;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;


@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchivedNotification {

    @Id
    private String id;
    @Indexed
    private Long senderId ;
    private String senderName;

    @Indexed
    private Long receiverId;
    @Indexed
    private Date createdAt;

    private Date archivedAt;
    private NotificationType type;
}
