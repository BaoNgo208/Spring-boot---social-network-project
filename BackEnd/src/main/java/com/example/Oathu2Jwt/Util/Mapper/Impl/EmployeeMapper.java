package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.UserDTO;
import com.example.Oathu2Jwt.Model.Entity.User.UserEntity;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper implements Mapper<UserEntity, UserDTO> {
    private final ModelMapper modelMapper;
    @Override
    public UserDTO mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public UserEntity mapFrom(UserDTO employeeDTO) {
        return modelMapper.map(employeeDTO, UserEntity.class);
    }
}
