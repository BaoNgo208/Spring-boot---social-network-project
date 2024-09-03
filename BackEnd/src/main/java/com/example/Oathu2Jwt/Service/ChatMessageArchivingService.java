package com.example.Oathu2Jwt.Service;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import org.springframework.data.domain.Page;

public interface ChatMessageArchivingService {

    public void archiveOldChatMessage();
    public Page<ChatMessage> getMessagesBetweenUsers(String userId1, String userId2, int page, int size);
    public void saveMessageToMongodbAndRedis(ChatMessage chatMessage);
}
