package com.example.Oathu2Jwt.Model.DTO;

import com.example.Oathu2Jwt.Model.Entity.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
