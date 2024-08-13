package com.example.Oathu2Jwt.Service;

import com.example.Oathu2Jwt.Model.Entity.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    public UserInfoEntity getUserByEmail(String email);
    public EmployeeEntity updateEmployee(String id , EmployeeEntity employee);
    public String  addFriend(String user,String accName);
    public List<UserInfoEntity> getAddFriendRequestList(String email);
    public List<FriendListAndMutualFriend> acceptFriendRequest(String emailId,Long userSecondId);
    public String deleteFriendRequest(String user1Email , String user2Email);
    public List<UserInfoEntity> getFriendList(String emailId);
    public List<UserInfoEntity> getSearchResult(String username);
    public List<FriendListAndMutualFriend> createSocialGraph(String emailId);
    public List<FriendListAndMutualFriend> getFriendListAndMutualFriend(String emailId);


}
