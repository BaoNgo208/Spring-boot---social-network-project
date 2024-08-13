package com.example.Oathu2Jwt.Model.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    @NotEmpty
    private UserInfoDTO user;

    @NotEmpty
    private Date createAt;
}
