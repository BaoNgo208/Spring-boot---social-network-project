package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ArchivedChatMessages;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ArchivedChatMessagesRepo;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ChatMessageRepo;
import com.example.Oathu2Jwt.Service.ChatMessageArchivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class ChatMessageArchivingServiceImpl implements ChatMessageArchivingService {
    private final ChatMessageRepo chatMessageRepo;
    private final ArchivedChatMessagesRepo archivedChatMessagesRepo;
    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void archiveOldChatMessage() {
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));
        System.out.println("arc:arc");
        List<ChatMessage> oldMessages = chatMessageRepo.findByCreatedAtBefore(thirtyDaysAgo);
        for (ChatMessage message : oldMessages) {
            ArchivedChatMessages archivedChatMessage = ArchivedChatMessages.builder()
                    .id(message.getId())
                    .senderId(message.getSenderId())
                    .receiverId(message.getReceiverId())
                    .content(message.getContent())
                    .createdAt(message.getCreatedAt())
                    .archivedAt(new Date())
                    .build();

            archivedChatMessagesRepo.save(archivedChatMessage);
            chatMessageRepo.delete(message);
        }
    }
}
