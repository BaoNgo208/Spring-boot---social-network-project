package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.EmployeeDTO;
import com.example.Oathu2Jwt.Model.Entity.User.EmployeeEntity;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper implements Mapper<EmployeeEntity, EmployeeDTO> {
    private final ModelMapper modelMapper;
    @Override
    public EmployeeDTO mapTo(EmployeeEntity employeeEntity) {
        return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    @Override
    public EmployeeEntity mapFrom(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, EmployeeEntity.class);
    }
}
