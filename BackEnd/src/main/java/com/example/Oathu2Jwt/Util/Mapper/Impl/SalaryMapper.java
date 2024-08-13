package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.SalaryDTO;
import com.example.Oathu2Jwt.Model.Entity.Salary;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalaryMapper implements Mapper<Salary, SalaryDTO> {
    private final ModelMapper modelMapper;
    @Override
    public SalaryDTO mapTo(Salary salary) {
        return modelMapper.map(salary,SalaryDTO.class);
    }

    @Override
    public Salary mapFrom(SalaryDTO salaryDTO) {
        return modelMapper.map(salaryDTO,Salary.class);
    }
}
