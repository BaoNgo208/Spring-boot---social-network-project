package com.example.Oathu2Jwt.Model.Entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateHistory {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date UpdateTime;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Comment comment;
}
