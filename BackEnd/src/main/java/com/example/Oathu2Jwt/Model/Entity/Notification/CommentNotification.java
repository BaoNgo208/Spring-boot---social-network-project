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
public class CommentNotification extends Notification{
    private CommentDTO comment;
    public CommentNotification(Long senderId,String senderName, Long receiverId, CommentDTO comment) {
        super(senderId, senderName, receiverId, NotificationType.COMMENT);
        this.comment = comment;
    }
}
