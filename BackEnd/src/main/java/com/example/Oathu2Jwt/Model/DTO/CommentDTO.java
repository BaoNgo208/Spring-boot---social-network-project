package com.example.Oathu2Jwt.Model.DTO;


import com.example.Oathu2Jwt.Model.Entity.UpdateHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Date CommentTime;
    private UserInfoDTO user;
    private List<UpdateHistoryDTO> updateHistories;

}
