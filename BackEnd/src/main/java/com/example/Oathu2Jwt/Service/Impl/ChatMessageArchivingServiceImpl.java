package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ArchivedChatMessages;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ArchivedChatMessagesRepo;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ChatMessageRepo;
import com.example.Oathu2Jwt.Service.ChatMessageArchivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatMessageArchivingServiceImpl implements ChatMessageArchivingService {
    private final ChatMessageRepo chatMessageRepo;
    private final ArchivedChatMessagesRepo archivedChatMessagesRepo;
    private final RedisTemplate<String,Object> redisTemplate;

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

    @Override
    public Page<ChatMessage> getMessagesBetweenUsers(String userId1, String userId2, int page, int size) {
            Pageable pageable = PageRequest.of(page, size);

            String redisKey1 = userId1 + ":" + userId2;
            String redisKey2 = userId2 + ":" + userId1;

            List<Object> chatMessagesFromRedis = new ArrayList<>();
            chatMessagesFromRedis.addAll(redisTemplate.opsForHash().values(redisKey1));
            chatMessagesFromRedis.addAll(redisTemplate.opsForHash().values(redisKey2));

            List<ChatMessage> messages = chatMessagesFromRedis.stream()
                    .filter(obj -> obj instanceof ChatMessage)
                    .map(obj -> (ChatMessage) obj)
                    .collect(Collectors.toList());

            // If no messages found in Redis, fetch from MongoDB
            if (messages.isEmpty()) {
                messages = chatMessageRepo.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
                        userId1, userId2, userId2, userId1, pageable).getContent();
            }

            if (messages.isEmpty()) {
            // Trả về một Page<ChatMessage> trống
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }

            messages.sort((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));
            // Implement pagination manually
            int start = Math.min((int) pageable.getOffset(), messages.size());
            int end = Math.min((start + pageable.getPageSize()), messages.size());
            List<ChatMessage> pagedMessages = messages.subList(start, end);

            return new PageImpl<>(pagedMessages, pageable, messages.size());
    }

}
