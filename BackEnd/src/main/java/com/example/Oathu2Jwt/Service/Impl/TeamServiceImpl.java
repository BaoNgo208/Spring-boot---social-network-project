package com.example.Oathu2Jwt.Service.Impl;

import com.example.Oathu2Jwt.Model.Entity.EmployeeEntity;
import com.example.Oathu2Jwt.Model.Entity.Task;
import com.example.Oathu2Jwt.Model.Entity.Team;
import com.example.Oathu2Jwt.Model.Entity.UserInfoEntity;
import com.example.Oathu2Jwt.Repository.EmployeeRepo;
import com.example.Oathu2Jwt.Repository.TaskRepo;
import com.example.Oathu2Jwt.Repository.TeamRepo;
import com.example.Oathu2Jwt.Repository.UserInfoRepo;
import com.example.Oathu2Jwt.Service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {
    private final TeamRepo teamRepo;
    private final UserInfoRepo userInfoRepo;
    private final TaskRepo taskRepo;

    @Override
    public Team getTeamByName(String name) {
        return teamRepo.findByTeamName(name);
    }

    @Override
    public Team createTeam(Team team) {

        team.getTask().setTeam(team);
        List<UserInfoEntity> empList= new ArrayList<>();

        int currentSize= team.getTeamMember().size();
        for (int i = 0 ; i< currentSize ; i++) {
            UserInfoEntity employee = userInfoRepo.findByEmailId(team.getTeamMember().get(i).getEmailId()).get();
            empList.add(employee);
            employee.setTeam(team);
        }
        team.setTeamMember(empList);
        return teamRepo.save(team);
    }

    @Override
    @Transactional
    public Team updateTeam(String id,Team team) {
        List<UserInfoEntity> userList= new ArrayList<>();
        int currentSize = team.getTeamMember().size();
        for(int i = 0 ; i< currentSize;i++) {
            UserInfoEntity employee = userInfoRepo.findByEmailId(team.getTeamMember().get(i).getEmailId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            userList.add(employee);
        }
        return teamRepo.findById(Long.parseLong(id)).map(existingTeam -> {
            Optional.ofNullable(team.getTeamName()).ifPresent(existingTeam::setTeamName);
            existingTeam.setTeamMember(userList);
            for(int i = 0 ; i < existingTeam.getTeamMember().size() ; i ++ ) {
                userList.get(i).setTeam(existingTeam);
            }
            existingTeam.setTask(updateTask(team));
            return teamRepo.save(existingTeam);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"team not found"));
    }
    public Task updateTask(Team team){

        Task existingTask = taskRepo.findById(team.getTask().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "team not found"));
        Optional.ofNullable(team.getTask().getStartDate()).ifPresent(existingTask::setStartDate);
        Optional.ofNullable(team.getTask().getEstimatedDate()).ifPresent(existingTask::setEstimatedDate);
        Optional.ofNullable(team.getTask().getCompleteDate()).ifPresent(existingTask::setCompleteDate);
        Optional.ofNullable(team.getTask().getTaskName()).ifPresent(existingTask::setTaskName);
        Optional.ofNullable(team.getTask().getStatus()).ifPresent(existingTask::setStatus);
        Optional.ofNullable(team.getTask().getTaskDescription()).ifPresent(existingTask::setTaskDescription);

        return  existingTask;
    }
    public int getPost(UserInfoEntity userInfo,String teamName) {
        Team team = teamRepo.findByTeamName(teamName);
        for(int i  = 0 ; i< team.getTeamMember().size(); i++ ) {
            if(Objects.equals(userInfo.getId(), team.getTeamMember().get(i).getId())) {
                return i ;
            }
        }
        return 0;
    }



    @Override
    public Team removeMemberFromTeam(String teamName, String memId) {


        Team team = teamRepo.findByTeamName(teamName);
        var userInfo = userInfoRepo.findById(Long.parseLong(memId)).orElseThrow(() -> {
            log.error("[AuthService:userSignInAuth] User not found");
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"USER NOT FOUND ");
    });

        if(getPost(userInfo,teamName) == 0 ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user id not found");
        }
        userInfo.setTeam(null);
        team.getTeamMember().remove(getPost(userInfo,teamName));
        return teamRepo.save(team);
    }


}
