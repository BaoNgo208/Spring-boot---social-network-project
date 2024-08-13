package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.FriendListAndMutualFriendDTO;
import com.example.Oathu2Jwt.Model.DTO.UserInfoDTO;
import com.example.Oathu2Jwt.Model.Entity.FriendListAndMutualFriend;
import com.example.Oathu2Jwt.Model.Entity.UserInfoEntity;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FriendAndMutualFriendMapper implements Mapper<FriendListAndMutualFriend, FriendListAndMutualFriendDTO> {
    private final ModelMapper modelMapper;
    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<FriendListAndMutualFriend, FriendListAndMutualFriendDTO>() {
            @Override
            protected void configure() {
                using(ctx -> mapUserInfoEntityToDTO((UserInfoEntity) ctx.getSource()))
                        .map(source.getUserInfoEntity(), destination.getUserInfoDTO());
                using(ctx -> mapUserInfoEntityListToDTOList((List<UserInfoEntity>) ctx.getSource()))
                        .map(source.getMututalFriendList(), destination.getMututalFriendList());
            }
        });
    }
    private UserInfoDTO mapUserInfoEntityToDTO(UserInfoEntity userInfoEntity) {
        return modelMapper.map(userInfoEntity, UserInfoDTO.class);
    }
    private List<UserInfoDTO> mapUserInfoEntityListToDTOList(List<UserInfoEntity> userInfoEntityList) {
        return userInfoEntityList.stream()
                .map(this::mapUserInfoEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FriendListAndMutualFriendDTO mapTo(FriendListAndMutualFriend friendListAndMutualFriend) {
        return modelMapper.map(friendListAndMutualFriend,FriendListAndMutualFriendDTO.class);
    }

    @Override
    public FriendListAndMutualFriend mapFrom(FriendListAndMutualFriendDTO friendListAndMutualFriendDTO) {
        return modelMapper.map(friendListAndMutualFriendDTO,FriendListAndMutualFriend.class);
    }
}
