package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.EmployeeEntity;
import com.example.Oathu2Jwt.Service.EmployeeService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<EmployeeEntity,Long> {
        public EmployeeEntity findByUserName(String username);
}
