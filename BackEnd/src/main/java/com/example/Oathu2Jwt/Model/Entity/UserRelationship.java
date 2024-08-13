package com.example.Oathu2Jwt.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserRelationshipId.class)
public class UserRelationship {
    @Id
    @ManyToOne
    private UserInfoEntity userFirstId;

    @Id
    @ManyToOne
    private UserInfoEntity userSecondId;
    @Enumerated(EnumType.STRING)
    private Type type;


}
