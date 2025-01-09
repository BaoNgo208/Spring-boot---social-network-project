package com.example.Oathu2Jwt.Model.Entity.User;


import com.example.Oathu2Jwt.Model.Entity.Post;
import com.example.Oathu2Jwt.Model.Entity.RefreshTokenEntity;
import com.example.Oathu2Jwt.Model.Entity.Team;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USER_INFO")
public class UserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L; // Thêm dòng này

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, name = "EMAIL_ID", unique = true)
    private String emailId;

    @Column(nullable = false, name = "PASSWORD")
    private String password;
    @Column(unique = true)
    private String accName;

    @Column(nullable = false, name = "ROLES")
    private String roles;

    @OneToOne(mappedBy = "userInfo",cascade  =  CascadeType.ALL)
    @JoinColumn
    @JsonBackReference // Bỏ qua trường này khi serialize UserEntity
    private UserEntity employee;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    @JsonIgnore // Bỏ qua khi serialize
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore // Bỏ qua khi serialize

    private List<RefreshTokenEntity> refreshTokens;
    @JsonIgnore // Bỏ qua khi serialize

    @ManyToOne
    private Team team;
}
