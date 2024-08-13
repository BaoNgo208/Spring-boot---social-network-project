package com.example.Oathu2Jwt.Model.Entity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Salary implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private Long salary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date toDate;

    @OneToMany(mappedBy = "salary", cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Set<EmployeeEntity> employeeEntitySet = new HashSet<>();


}
