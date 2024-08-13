package com.example.Oathu2Jwt.Model.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkPoint {
    @Id
    @GeneratedValue
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date date;
    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime checkInTime;
    private boolean isLate;
    @ManyToOne
    private MonthlyWorkPoint monthlyCheckIn;

}
