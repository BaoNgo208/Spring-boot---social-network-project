package com.example.Oathu2Jwt.Model.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyWorkPoint {
    @Id
    @GeneratedValue
    private Long id ;

    private Integer numOfCheckIn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM")
    private Date month;
    private Integer lateDays;

    @OneToMany(mappedBy = "monthlyCheckIn",cascade = CascadeType.ALL)
    private List<WorkPoint> workPoints;

    @ManyToOne
    private UserInfoEntity userInfo;

}
