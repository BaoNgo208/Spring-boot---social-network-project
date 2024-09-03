package com.example.Oathu2Jwt.Repository.MongoDBRepo;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface ChatMessageRepo extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findByCreatedAtBefore(Date date);
    @Query("{ $or: [ { senderId: ?0, receiverId: ?1 }, { senderId: ?1, receiverId: ?0 } ] }")
    Page<ChatMessage> findMessagesBetweenUsers(
            Integer senderId,
            Integer receiverId,
            Pageable pageable);

}
