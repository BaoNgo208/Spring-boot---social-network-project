package com.example.Oathu2Jwt.Controller;

import com.example.Oathu2Jwt.Model.DTO.TeamDTO;
import com.example.Oathu2Jwt.Model.Entity.Team;
import com.example.Oathu2Jwt.Service.TeamService;
import com.example.Oathu2Jwt.Util.Mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
@Slf4j
public class TeamController {
    private final Mapper<Team,TeamDTO> mapper;
    private final TeamService teamService;

    @GetMapping("/get/{name}")
    public ResponseEntity<TeamDTO> getTeamByName(@PathVariable("name") String name ) {
         return ResponseEntity.ok(mapper.mapTo(teamService.getTeamByName(name)));
    }
    @PostMapping("/create")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
            Team team = mapper.mapFrom(teamDTO);
            Team savedTeam = teamService.createTeam(team);
            return new ResponseEntity<TeamDTO>(mapper.mapTo(savedTeam) , HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable("id") String id ,@RequestBody TeamDTO teamDTO) {
        Team team = mapper.mapFrom(teamDTO);
        return ResponseEntity.ok(mapper.mapTo(teamService.updateTeam(id,team)));
    }

    @PatchMapping("/delete/{teamName}/{memId}")
    public ResponseEntity<TeamDTO> removeMemberFromTeam(@PathVariable("teamName") String teamId ,
                                                        @PathVariable("memId") String memId) {

        return ResponseEntity.ok(mapper.mapTo(teamService.removeMemberFromTeam(teamId,memId)));
    }




}
