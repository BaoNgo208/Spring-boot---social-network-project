package com.example.Oathu2Jwt.Model.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String content;
    private Date PostTime;
    private String category;
    private UserInfoDTO user;
    private List<CommentDTO> comment;
    private List<UpdateHistoryDTO> updateHistories;
    private List<LikeDTO> likes;
}
