package com.example.Oathu2Jwt.Controller;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ChatMessageRepo;
import com.example.Oathu2Jwt.Service.ChatMessageArchivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;


@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepo chatMessageRepo;
    private final ChatMessageArchivingService chatMessageArchivingService;
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        chatMessageArchivingService.saveMessageToMongodbAndRedis(chatMessage);
        messagingTemplate.convertAndSend("/topic/chat", chatMessage);
    }

    @GetMapping("/get/messages")
    public Page<ChatMessage> getMessagesBetweenUsers(
            @RequestParam("userId1") String userId1,
            @RequestParam("userId2") String userId2,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        return chatMessageArchivingService.getMessagesBetweenUsers(userId1, userId2, page, size);
    }

}
