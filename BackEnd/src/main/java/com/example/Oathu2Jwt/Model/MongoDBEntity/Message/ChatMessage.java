package com.example.Oathu2Jwt.Model.MongoDBEntity.Message;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    private String id;
    @Indexed
    private Integer senderId;
    @Indexed

    private Integer receiverId;
    private String content;

}