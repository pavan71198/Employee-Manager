package com.example.empmanager.service;

import java.util.List;

import com.example.empmanager.dto.EmployeeRequestDto;
import com.example.empmanager.dto.EmployeeResponseDto;

public interface EmployeeService {

    String addNewEmployee (EmployeeRequestDto newEmployeeRequestDto);

    List<EmployeeResponseDto> fetchAllEmployees();

    EmployeeResponseDto fetchEmployeeById(String id);

    String updateEmployee(EmployeeResponseDto newEmployeeResponseDto);

    String deleteEmployee (String id);
}
