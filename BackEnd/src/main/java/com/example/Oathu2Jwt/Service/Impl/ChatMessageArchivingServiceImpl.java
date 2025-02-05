package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Exception.RedisOperationException;
import com.example.Oathu2Jwt.Exception.UserNotFoundException;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Chat.Chat;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ArchivedChatMessages;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ArchivedChatMessagesRepo;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ChatMessageRepo;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ChatRepo;
import com.example.Oathu2Jwt.Service.ChatMessageArchivingService;
import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatMessageArchivingServiceImpl implements ChatMessageArchivingService {
    private final ChatMessageRepo chatMessageRepo;
    private final ArchivedChatMessagesRepo archivedChatMessagesRepo;
    private final RedisTemplate<String,Object> redisTemplate;
    private final ChatRepo chatRepo;


    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void archiveOldChatMessage() {
        Date thirtyDaysAgo = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));
        List<ChatMessage> oldMessages = chatMessageRepo.findByCreatedAtBefore(thirtyDaysAgo);
        System.out.println("archived chat size:" + oldMessages.size());
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
    private ChatMessage convertToChatMessage(ArchivedChatMessages archivedMessage) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(archivedMessage.getId());
        chatMessage.setSenderId(archivedMessage.getSenderId());
        chatMessage.setReceiverId(archivedMessage.getReceiverId());
        chatMessage.setContent(archivedMessage.getContent());
        chatMessage.setCreatedAt(archivedMessage.getCreatedAt());
        return chatMessage;
    }
    @Override
    public Page<ChatMessage> getMessagesBetweenUsers(String userId1, String userId2, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        try {
            String redisKey1 = userId1 + ":" + userId2;
            String redisKey2 = userId2 + ":" + userId1;

            List<Object> chatMessagesFromRedis = new ArrayList<>();

            if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey1))) {
                chatMessagesFromRedis.addAll(redisTemplate.opsForHash().values(redisKey1));
            }
            if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey2))) {
                chatMessagesFromRedis.addAll(redisTemplate.opsForHash().values(redisKey2));
            }

            Set<ChatMessage> messagesSet = chatMessagesFromRedis.stream()
                    .filter(obj -> obj instanceof ChatMessage)
                    .map(obj -> (ChatMessage) obj)
                    .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ChatMessage::getId))));

            List<ChatMessage> messages = new ArrayList<>(messagesSet);

            if (messages.isEmpty()) {
                Page<ChatMessage> pageOfMessages = chatMessageRepo.findMessagesBetweenUsers(
                        Integer.parseInt(userId1),
                        Integer.parseInt(userId2),
                        pageable);
                messages = pageOfMessages.getContent();

                for (ChatMessage msg : messages) {
                    String key = msg.getSenderId().toString() + ":" + msg.getReceiverId().toString();
                    redisTemplate.opsForHash().put(key, String.valueOf(msg.getId()), msg);
                    redisTemplate.expire(key, Duration.ofDays(7));
                }
            } else {
                Page<ChatMessage> pageOfMessages = chatMessageRepo.findMessagesBetweenUsers(
                        Integer.parseInt(userId1),
                        Integer.parseInt(userId2),
                        pageable);
                messagesSet.addAll(pageOfMessages.getContent());
                messages = new ArrayList<>(messagesSet);
            }

            if (messages.isEmpty()) {
                Page<ArchivedChatMessages> pageOfMessages = archivedChatMessagesRepo.findMessagesBetweenUsers(
                        Integer.parseInt(userId1),
                        Integer.parseInt(userId2),
                        pageable);
                messages = pageOfMessages.getContent().stream()
                        .map(this::convertToChatMessage)
                        .toList();
            }

            List<ChatMessage> sortedMessages = new ArrayList<>(messages);
            sortedMessages.sort(Comparator.comparing(ChatMessage::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), sortedMessages.size());
            if (start > sortedMessages.size()) {
                return new PageImpl<>(new ArrayList<>(), pageable, sortedMessages.size());
            }
            List<ChatMessage> pagedMessages = sortedMessages.subList(start, end);
            return new PageImpl<>(pagedMessages, pageable, sortedMessages.size());

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid userId format. userId1: " + userId1 + ", userId2: " + userId2 + ". Must be a valid integer.", e);
        } catch (RedisConnectionFailureException e) {
            throw new RuntimeException("Failed to connect to Redis. Please check the Redis server connection.", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching messages between users.", e);
        }
    }


    @Override
    public void saveMessageToMongodbAndRedis(ChatMessage chatMessage) {
        try {
            chatMessageRepo.save(chatMessage);
            Chat chat = chatRepo.
                    findByParticipantsContainingIgnoreOrder(
                            List.of(chatMessage.getReceiverId(),chatMessage.getSenderId()
                            )).orElseThrow(() -> new UserNotFoundException("123"));

            chat.setLastMessage(chatMessage);
            chatRepo.save(chat);
            String redisKey = chatMessage.getSenderId().toString() + ":" + chatMessage.getReceiverId().toString();
            redisTemplate.opsForHash().put(redisKey, chatMessage.getId(), chatMessage);
            redisTemplate.expire(redisKey, Duration.ofDays(7));
        }
        catch(MongoException e) {
            throw e;
        }
        catch (Exception exception) {
            throw new RedisOperationException("Failed to save to Redis");
        }
    }
}
