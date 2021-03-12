package com.example.empmanager.service;

import java.util.*;
import java.util.regex.Pattern;

import com.example.empmanager.entity.Employee;
import com.example.empmanager.exception.EmployeeNotFoundException;
import com.example.empmanager.repository.EmployeeRepository;
import com.example.empmanager.dto.EmployeeRequestDto;
import com.example.empmanager.dto.EmployeeResponseDto;
import com.example.empmanager.util.EmployeeDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.CollectionUtils;

public interface EmployeeService {

    public String addNewEmployee (EmployeeRequestDto newEmployeeRequestDto);

    public List<EmployeeResponseDto> fetchAllEmployees();

    public EmployeeResponseDto fetchEmployeeById(String id);

    public String updateEmployee(EmployeeRequestDto newEmployeeRequestDto, String id);

    public String deleteEmployee (String id);
}
