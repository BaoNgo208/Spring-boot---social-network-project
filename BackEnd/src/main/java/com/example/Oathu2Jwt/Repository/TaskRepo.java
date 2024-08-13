package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task,Long> {
}
