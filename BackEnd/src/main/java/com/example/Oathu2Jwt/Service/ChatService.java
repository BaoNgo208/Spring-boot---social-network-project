package com.example.Oathu2Jwt.Service;

import com.example.Oathu2Jwt.Model.DTO.ChatWithUserInfo;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Chat.Chat;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ChatService {
    public Page<ChatWithUserInfo> getChatsByUserId(String userName, Integer userId, int page, int size);
}
