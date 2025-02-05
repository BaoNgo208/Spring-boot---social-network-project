package com.example.Oathu2Jwt.Controller;

import com.example.Oathu2Jwt.Config.userConfig.UserInfoConfig;
import com.example.Oathu2Jwt.Model.DTO.ChatWithUserInfo;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Chat.Chat;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ChatMessageRepo;
import com.example.Oathu2Jwt.Service.ChatMessageArchivingService;
import com.example.Oathu2Jwt.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.Duration;


@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageArchivingService chatMessageArchivingService;
    private final ChatService chatService;
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

    @GetMapping("/get/chat")
    public Page<ChatWithUserInfo> getAllChatOfUser(
                                        @RequestParam Integer userId,
                                       @RequestParam String userName,
                                       @RequestParam int page ,
                                       @RequestParam int size) {

        return chatService.getChatsByUserId(userName,userId,page,size);
    }

}
