package com.example.Oathu2Jwt.Repository;

import com.example.Oathu2Jwt.Model.Entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepo extends JpaRepository<Salary,Long> {
    public Salary findBySalary(Long salary);
}
