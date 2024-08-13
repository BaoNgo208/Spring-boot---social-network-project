package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.CommentDTO;
import com.example.Oathu2Jwt.Model.Entity.Comment;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper implements Mapper<Comment, CommentDTO> {
    private final ModelMapper modelMapper;
    @Override
    public CommentDTO mapTo(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public Comment mapFrom(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO,Comment.class);
    }


}
