package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.UpdateHistoryDTO;
import com.example.Oathu2Jwt.Model.Entity.UpdateHistory;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateHistoryMapper implements Mapper<UpdateHistory, UpdateHistoryDTO> {
    private final ModelMapper modelMapper;
    @Override
    public UpdateHistoryDTO mapTo(UpdateHistory updateHistory) {
        return modelMapper.map(updateHistory, UpdateHistoryDTO.class);
    }

    @Override
    public UpdateHistory mapFrom(UpdateHistoryDTO updateHistoryDTO) {
        return modelMapper.map(updateHistoryDTO,UpdateHistory.class);
    }
}
