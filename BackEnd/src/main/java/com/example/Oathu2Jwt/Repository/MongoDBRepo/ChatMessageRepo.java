package com.example.Oathu2Jwt.Repository.MongoDBRepo;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepo extends MongoRepository<ChatMessage,String> {
}
