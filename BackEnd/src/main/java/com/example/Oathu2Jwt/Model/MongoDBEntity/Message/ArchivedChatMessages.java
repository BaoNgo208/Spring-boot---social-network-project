package com.example.Oathu2Jwt.Model.MongoDBEntity.Message;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchivedChatMessages {
    @Id
    private String id;

    @Indexed
    private Integer senderId;

    @Indexed
    private Integer receiverId;

    private String content;

    @Indexed
    private Date createdAt;

    @Indexed
    private Date archivedAt;
}
