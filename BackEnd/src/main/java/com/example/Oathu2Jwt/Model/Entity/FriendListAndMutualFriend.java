package com.example.Oathu2Jwt.Model.Entity;

import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FriendListAndMutualFriend   implements Serializable {
    private static final long serialVersionUID = 1L; // Thêm dòng này

    private Integer mutualFriend ;
    private UserInfoEntity userInfoEntity;
    private List<UserInfoEntity> mututalFriendList;
}
