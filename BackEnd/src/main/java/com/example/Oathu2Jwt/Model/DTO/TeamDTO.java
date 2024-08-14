package com.example.Oathu2Jwt.Model.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {
    private Long id;
    @NotEmpty(message = "Team Name must not be empty")
    private String teamName;


    private List<UserInfoDTO> teamMember ;

    private TaskDTO task;
}
