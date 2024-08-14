package com.example.Oathu2Jwt.Model.Entity;

import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String teamName;

    @OneToMany(mappedBy = "team",cascade = CascadeType.ALL)
    private List<UserInfoEntity> teamMember ;

    @OneToOne(mappedBy = "team" , cascade = CascadeType.ALL)
    private Task task;
}
