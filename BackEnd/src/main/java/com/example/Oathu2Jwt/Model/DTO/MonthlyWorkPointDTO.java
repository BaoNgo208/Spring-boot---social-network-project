package com.example.Oathu2Jwt.Model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyWorkPointDTO {
    private Integer numOfCheckIn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM-yyyy")
    private Date month;
    private Integer lateDays;
    private List<WorkPointDTO> workPoints;

    private UserInfoDTO userInfo;
}
