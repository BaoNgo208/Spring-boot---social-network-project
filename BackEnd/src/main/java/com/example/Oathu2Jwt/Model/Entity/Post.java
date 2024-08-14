package com.example.Oathu2Jwt.Model.Entity;

import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private Long id ;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date PostTime;
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfoEntity user;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> comment;

    @OneToMany(mappedBy = "post" , cascade = CascadeType.ALL)
    private List<UpdateHistory> updateHistories;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeEntity> likes;
}
