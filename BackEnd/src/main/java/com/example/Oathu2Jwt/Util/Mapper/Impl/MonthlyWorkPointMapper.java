package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.MonthlyWorkPointDTO;
import com.example.Oathu2Jwt.Model.Entity.MonthlyWorkPoint;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyWorkPointMapper implements Mapper<MonthlyWorkPoint, MonthlyWorkPointDTO> {
    private final ModelMapper modelMapper;

    @Override
    public MonthlyWorkPointDTO mapTo(MonthlyWorkPoint monthlyWorkPoint) {
        return modelMapper.map(monthlyWorkPoint,MonthlyWorkPointDTO.class);
    }

    @Override
    public MonthlyWorkPoint mapFrom(MonthlyWorkPointDTO monthlyWorkPointDTO) {
        return modelMapper.map(monthlyWorkPointDTO,MonthlyWorkPoint.class);
    }
}
