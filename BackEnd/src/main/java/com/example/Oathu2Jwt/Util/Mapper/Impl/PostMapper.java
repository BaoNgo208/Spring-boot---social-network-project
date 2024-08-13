package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.PostDTO;
import com.example.Oathu2Jwt.Model.Entity.Post;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class PostMapper implements Mapper<Post, PostDTO> {
    private final ModelMapper modelMapper;

    @Override
    public PostDTO mapTo(Post post) {
        return modelMapper.map(post,PostDTO.class);
    }

    @Override
    public Post mapFrom(PostDTO postDTO) {
        return modelMapper.map(postDTO,Post.class);
    }
}
