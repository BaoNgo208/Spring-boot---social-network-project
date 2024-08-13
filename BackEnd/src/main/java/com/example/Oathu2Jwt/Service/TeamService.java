package com.example.Oathu2Jwt.Service;



import com.example.Oathu2Jwt.Model.Entity.Team;

public interface TeamService {
    public Team getTeamByName(String name);
    public Team createTeam(Team team);
    public Team updateTeam(String id,Team team);
    public Team removeMemberFromTeam(String teamName,String memId);

}
