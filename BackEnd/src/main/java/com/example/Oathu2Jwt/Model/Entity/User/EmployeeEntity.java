package com.example.Oathu2Jwt.Model.Entity.User;

import com.example.Oathu2Jwt.Model.Entity.Salary;
import com.example.Oathu2Jwt.Model.Entity.User.UserInfoEntity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date dateOfBirth;
    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @ManyToOne
    private Salary salary;

    @OneToOne
    private UserInfoEntity userInfo;


}
