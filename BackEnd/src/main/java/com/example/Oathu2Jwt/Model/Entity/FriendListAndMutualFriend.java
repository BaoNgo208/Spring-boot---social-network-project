package com.example.Oathu2Jwt.Model.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FriendListAndMutualFriend   implements Serializable {

    private Integer mutualFriend ;
    private UserInfoEntity userInfoEntity;
    private List<UserInfoEntity> mututalFriendList;
}
