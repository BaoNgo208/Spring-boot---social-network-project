package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.UserInfoDTO;
import com.example.Oathu2Jwt.Model.Entity.UserInfoEntity;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInfoMapper implements Mapper<UserInfoEntity, UserInfoDTO> {
    private final ModelMapper modelMapper;

    @Override
    public UserInfoDTO mapTo(UserInfoEntity userInfoEntity) {
        return modelMapper.map(userInfoEntity,UserInfoDTO.class);
    }

    @Override
    public UserInfoEntity mapFrom(UserInfoDTO userInfoDTO) {
        return modelMapper.map(userInfoDTO,UserInfoEntity.class);
    }
}
