package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.LikeDTO;
import com.example.Oathu2Jwt.Model.Entity.LikeEntity;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeMapper implements Mapper<LikeEntity, LikeDTO> {
    private final ModelMapper modelMapper;
    @Override
    public LikeDTO mapTo(LikeEntity likeEntity) {
        return modelMapper.map(likeEntity,LikeDTO.class);
    }

    @Override
    public LikeEntity mapFrom(LikeDTO likeDTO) {
        return modelMapper.map(likeDTO, LikeEntity.class);
    }
}
