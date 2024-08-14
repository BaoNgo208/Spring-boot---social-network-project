package com.example.Oathu2Jwt.Model.Entity.Notification;

import com.example.Oathu2Jwt.Model.DTO.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentNotification extends PostRelatedNotification {
    private CommentDTO comment;

    public CommentNotification(Long senderId, String senderName, Long receiverId, NotificationType type, Long postId, CommentDTO comment) {
        super(senderId, senderName, receiverId, type, postId);
        this.comment = comment;
    }
}