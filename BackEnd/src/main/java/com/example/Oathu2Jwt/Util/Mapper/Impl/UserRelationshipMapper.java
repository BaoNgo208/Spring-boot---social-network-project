package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.UserRelationshipDTO;
import com.example.Oathu2Jwt.Model.Entity.User.UserRelationship;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRelationshipMapper implements Mapper<UserRelationship, UserRelationshipDTO> {
    private final ModelMapper modelMapper;
    @Override
    public UserRelationshipDTO mapTo(UserRelationship userRelationship) {
        return modelMapper.map(userRelationship,UserRelationshipDTO.class);
    }

    @Override
    public UserRelationship mapFrom(UserRelationshipDTO userRelationshipDTO) {
        return modelMapper.map(userRelationshipDTO, UserRelationship.class);
    }
}
