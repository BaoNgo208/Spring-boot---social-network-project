package com.example.Oathu2Jwt.Model.DTO;

import com.example.Oathu2Jwt.Model.Entity.UserInfoEntity;
import lombok.Data;

import java.util.List;

@Data
public class FriendListAndMutualFriendDTO {
    private Integer mutualFriend ;
    private UserInfoDTO userInfoDTO;
    private List<UserInfoDTO> mututalFriendList;
}
