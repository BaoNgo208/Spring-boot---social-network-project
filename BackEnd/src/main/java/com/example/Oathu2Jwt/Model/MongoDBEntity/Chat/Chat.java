package com.example.Oathu2Jwt.Model.MongoDBEntity.Chat;


import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import lombok.*;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {
    @Id
    private String id;
    private List<Integer> participants;
    private ChatMessage lastMessage;
    private Map<Integer, Integer> unreadCount;
}
