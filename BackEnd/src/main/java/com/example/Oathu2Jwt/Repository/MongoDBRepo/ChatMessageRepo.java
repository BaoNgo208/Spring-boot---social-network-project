package com.example.Oathu2Jwt.Repository.MongoDBRepo;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface ChatMessageRepo extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findByCreatedAtBefore(Date date);
}
