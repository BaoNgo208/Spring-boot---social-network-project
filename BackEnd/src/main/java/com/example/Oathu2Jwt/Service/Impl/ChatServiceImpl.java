package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Exception.UserNotFoundException;
import com.example.Oathu2Jwt.Model.DTO.ChatWithUserInfo;
import com.example.Oathu2Jwt.Model.DTO.UserInfoDTO;
import com.example.Oathu2Jwt.Model.Entity.User.UserEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.example.Oathu2Jwt.Model.MongoDBEntity.Chat.Chat;
import com.example.Oathu2Jwt.Repository.MongoDBRepo.ChatRepo;
import com.example.Oathu2Jwt.Repository.UserInfoRepo;
import com.example.Oathu2Jwt.Repository.UserRepo;
import com.example.Oathu2Jwt.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepo chatRepo;
    private final UserInfoRepo userInfoRepo;


    @Override
    public Page<ChatWithUserInfo> getChatsByUserId(String userName, Integer userId, int page, int size) {

//        Pageable pageable = PageRequest.of(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("lastMessage.createdAt")));
        Page<Chat> chatPage = chatRepo.findByUserId(userId, pageable);
        List<ChatWithUserInfo> chatWithUserInfoList = chatPage.getContent().stream().map(chat -> {
            int otherUserId = Objects.equals(chat.getParticipants().get(0), userId)
                    ? chat.getParticipants().get(1)
                    : chat.getParticipants().get(0);

            Map<Integer, String> users = new HashMap<>();
            users.put(userId, userName);

            userInfoRepo.findById((long) otherUserId)
                    .ifPresent(userB -> users.put(otherUserId, userB.getEmployee().getUserName()));

            return new ChatWithUserInfo(chat, users);
        }).toList();
        return new PageImpl<>(chatWithUserInfoList, pageable, chatPage.getTotalElements());
    }

}
