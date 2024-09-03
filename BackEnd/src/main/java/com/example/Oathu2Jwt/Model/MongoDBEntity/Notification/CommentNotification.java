package com.example.Oathu2Jwt.Model.MongoDBEntity.Notification;

import com.example.Oathu2Jwt.Model.DTO.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentNotification extends PostRelatedNotification {
    private CommentDTO comment;

    public CommentNotification(Long senderId, String senderName, Long receiverId, Date createdAt, NotificationType type, Long postId, CommentDTO comment) {
        super(senderId, senderName, receiverId,createdAt, type, postId);
        this.comment = comment;
    }
}