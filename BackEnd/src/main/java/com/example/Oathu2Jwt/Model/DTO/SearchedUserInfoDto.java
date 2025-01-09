package com.example.Oathu2Jwt.Model.DTO;

public class SearchedUserInfoDto {
    private String emailId;

    private String accName;

    private UserDTO user;

    public Integer getMutualFriend() {
        return mutualFriend;
    }

    public void setMutualFriend(Integer mutualFriend) {
        this.mutualFriend = mutualFriend;
    }

    private Integer mutualFriend;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Boolean getFriend() {
        return isFriend;
    }

    public void setFriend(Boolean friend) {
        isFriend = friend;
    }

    private Boolean isFriend;

    public SearchedUserInfoDto(String emailId, String accName, UserDTO user, Integer mutualFriend, Boolean isFriend) {
        this.emailId = emailId;
        this.accName = accName;
        this.user = user;
        this.mutualFriend = mutualFriend;
        this.isFriend = isFriend;
    }

    public SearchedUserInfoDto(String emailId, String accName, UserDTO user, Boolean isFriend) {
        this.emailId = emailId;
        this.accName = accName;
        this.user = user;
        this.isFriend = isFriend;
    }
}
