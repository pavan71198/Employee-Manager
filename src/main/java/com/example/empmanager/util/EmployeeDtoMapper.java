package com.example.empmanager.util;

import com.example.empmanager.dto.EmployeeRequestDto;
import com.example.empmanager.dto.EmployeeResponseDto;
import com.example.empmanager.entity.Employee;

public class EmployeeDtoMapper {
    public EmployeeDtoMapper() {

    }

    public EmployeeResponseDto toResponseDto(Employee employee){
        return new EmployeeResponseDto(employee.getId().toString(), employee.getName(), employee.getRole(), employee.getEmail());
    }
    public EmployeeRequestDto toRequestDto(Employee employee){
        return new EmployeeRequestDto(employee.getName(), employee.getRole(), employee.getEmail());
    }
    public Employee toEmployee(EmployeeRequestDto employeeRequestDto){
        return new Employee(employeeRequestDto.getName(), employeeRequestDto.getRole(), employeeRequestDto.getEmail());
    }
}
