package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepo extends JpaRepository<Team,Long> {
    public Team findByTeamName(String name);
}
