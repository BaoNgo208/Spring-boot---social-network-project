package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.WorkPointDTO;
import com.example.Oathu2Jwt.Model.Entity.WorkPoint;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkPointMapper implements Mapper<WorkPoint, WorkPointDTO> {
    private final ModelMapper modelMapper;
    @Override
    public WorkPointDTO mapTo(WorkPoint workPoint) {
        return modelMapper.map(workPoint,WorkPointDTO.class);
    }

    @Override
    public WorkPoint mapFrom(WorkPointDTO workPointDTO) {
        return modelMapper.map(workPointDTO,WorkPoint.class);
    }
}
