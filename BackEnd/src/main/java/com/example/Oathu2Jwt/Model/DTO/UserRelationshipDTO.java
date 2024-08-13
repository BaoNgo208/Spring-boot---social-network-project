package com.example.Oathu2Jwt.Model.DTO;

import com.example.Oathu2Jwt.Model.Entity.Type;
import com.example.Oathu2Jwt.Model.Entity.UserInfoEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationshipDTO {
    private UserInfoDTO userFirstId;

    private UserInfoDTO userSecondId;
    @Enumerated(EnumType.STRING)
    private Type type;
}
