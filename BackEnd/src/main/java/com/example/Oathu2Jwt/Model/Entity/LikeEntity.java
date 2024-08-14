package com.example.Oathu2Jwt.Model.Entity;


import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "likes",
        indexes = {
                @Index(name="idx_user_id",columnList = "user_id"),
                @Index(name="idx_post_id",columnList = "post_id"),
                @Index(name="idx_date",columnList = "create_at"),
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfoEntity userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",nullable = false)
    private Post post;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @Column(name = "create_at" , nullable = false)
    private Date date;
}
