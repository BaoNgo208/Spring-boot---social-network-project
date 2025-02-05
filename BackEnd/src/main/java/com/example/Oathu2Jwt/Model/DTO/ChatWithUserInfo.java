package com.example.Oathu2Jwt.Model.DTO;


import com.example.Oathu2Jwt.Model.MongoDBEntity.Chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatWithUserInfo {
    private Chat chat;
    private Map<Integer, String> users;
}
