package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.User.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<EmployeeEntity,Long> {
        public EmployeeEntity findByUserName(String username);
}
