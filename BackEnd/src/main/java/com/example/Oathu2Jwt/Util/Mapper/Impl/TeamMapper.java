package com.example.Oathu2Jwt.Util.Mapper.Impl;

import com.example.Oathu2Jwt.Model.DTO.TeamDTO;
import com.example.Oathu2Jwt.Model.Entity.Team;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMapper implements Mapper<Team, TeamDTO> {
    private final ModelMapper modelMapper;
    @Override
    public TeamDTO mapTo(Team team) {
        return modelMapper.map(team, TeamDTO.class);
    }

    @Override
    public Team mapFrom(TeamDTO teamDTO) {
        return modelMapper.map(teamDTO,Team.class);
    }
}
