package com.example.empmanager.repository;

import com.example.empmanager.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

}