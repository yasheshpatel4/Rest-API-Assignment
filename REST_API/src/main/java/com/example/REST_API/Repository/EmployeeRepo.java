package com.example.REST_API.Repository;

import com.example.REST_API.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

}
