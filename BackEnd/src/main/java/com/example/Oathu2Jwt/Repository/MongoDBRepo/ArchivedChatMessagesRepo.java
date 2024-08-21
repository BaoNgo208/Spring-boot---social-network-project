package com.example.Oathu2Jwt.Repository.MongoDBRepo;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ArchivedChatMessages;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArchivedChatMessagesRepo extends MongoRepository<ArchivedChatMessages,String> {
}
