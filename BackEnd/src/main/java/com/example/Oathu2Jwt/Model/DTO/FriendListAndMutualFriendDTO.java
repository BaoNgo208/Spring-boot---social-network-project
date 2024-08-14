package com.example.Oathu2Jwt.Model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class FriendListAndMutualFriendDTO {
    private Integer mutualFriend ;
    private UserInfoDTO userInfoDTO;
    private List<UserInfoDTO> mututalFriendList;
}
