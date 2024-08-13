package com.example.Oathu2Jwt.Model.DTO;

import com.example.Oathu2Jwt.Model.Entity.EmployeeEntity;
import com.example.Oathu2Jwt.Model.Entity.Task;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
