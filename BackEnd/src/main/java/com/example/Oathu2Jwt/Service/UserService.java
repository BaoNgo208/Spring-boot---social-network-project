package com.example.Oathu2Jwt.Service;

import com.example.Oathu2Jwt.Model.DTO.SearchedUserInfoDto;
import com.example.Oathu2Jwt.Model.Entity.*;
import com.example.Oathu2Jwt.Model.Entity.User.UserEntity;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    public UserInfoEntity getUserByEmail(String email);
    public UserEntity updateEmployee(String id , UserEntity user);
    public String  addFriend(String user,String accName);
    public List<UserInfoEntity> getAddFriendRequestList(String email);
    public List<FriendListAndMutualFriend> acceptFriendRequest(String emailId,Long userSecondId);
    public String deleteFriendRequest(String user1Email , String user2Email);
    public List<UserInfoEntity> getFriendList(String emailId);
    public Page<SearchedUserInfoDto> getSearchResult(String email, String username, int page, int size);
    public List<FriendListAndMutualFriend> createSocialGraph(String emailId);
    public List<FriendListAndMutualFriend> getFriendListAndMutualFriend(String emailId);


}
