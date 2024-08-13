package com.example.Oathu2Jwt.Model.Entity;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private Integer senderId;
    private Integer receiverId;
    private String content;

}