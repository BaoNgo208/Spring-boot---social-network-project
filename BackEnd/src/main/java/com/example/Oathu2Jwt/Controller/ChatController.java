package com.example.Oathu2Jwt.Controller;

import com.example.Oathu2Jwt.Model.MongoDBEntity.Message.ChatMessage;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ChatMessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepo chatMessageRepo;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        chatMessageRepo.save(chatMessage);
        messagingTemplate.convertAndSend("/topic/chat", chatMessage);
    }
}
