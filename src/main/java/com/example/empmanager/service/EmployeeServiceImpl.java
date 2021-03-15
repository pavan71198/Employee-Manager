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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    private final EmployeeDtoMapper employeeDtoMapper = new EmployeeDtoMapper();

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern emailPattern = Pattern.compile(emailRegex);

    public String addNewEmployee (EmployeeRequestDto newEmployeeRequestDto) {
        if (newEmployeeRequestDto.getName()!=null && newEmployeeRequestDto.getRole()!=null && newEmployeeRequestDto.getEmail()!=null) {
            if (!newEmployeeRequestDto.getName().isEmpty() && !newEmployeeRequestDto.getRole().isEmpty()) {
                if (emailPattern.matcher(newEmployeeRequestDto.getEmail()).matches()) {
                    Employee newEmployee = new Employee(newEmployeeRequestDto.getName(), newEmployeeRequestDto.getRole(), newEmployeeRequestDto.getEmail());
                    employeeRepository.save(newEmployee);
                    return "Saved: " + newEmployee.getId();
                }
                else {
                    return "Invalid Employee Email: " + newEmployeeRequestDto.toString();
                }
            }
            else {
                return "Invalid Employee Description: " + newEmployeeRequestDto.toString();
            }
        }
        else{
            return "Invalid Employee Description: " + newEmployeeRequestDto.toString();
        }
    }

    public List<EmployeeResponseDto> fetchAllEmployees(){
        List<EmployeeResponseDto> employeeResponseDtoList = new ArrayList<>();
        List<Employee> employeeList = employeeRepository.findAll();
        if (!CollectionUtils.isEmpty(employeeList)) {
            for (Employee employee : employeeList) {
                employeeResponseDtoList.add(employeeDtoMapper.toResponseDto(employee));
            }
        }
        return employeeResponseDtoList;
    }

    public EmployeeResponseDto fetchEmployeeById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<Employee> employeeMatch = employeeRepository.findById(uuid);
            if (employeeMatch.isPresent()) {
                return employeeDtoMapper.toResponseDto(employeeMatch.get());
            } else {
                throw new EmployeeNotFoundException(id);
            }
        }
        catch (IllegalArgumentException exception){
            throw new EmployeeNotFoundException(id);
        }
    }

    public String updateEmployee(EmployeeResponseDto newEmployeeResponseDto) {
        if (newEmployeeResponseDto.getName()!=null && newEmployeeResponseDto.getRole()!=null && newEmployeeResponseDto.getEmail()!=null) {
            if (!newEmployeeResponseDto.getName().isEmpty() && !newEmployeeResponseDto.getRole().isEmpty()) {
                if (emailPattern.matcher(newEmployeeResponseDto.getEmail()).matches()) {
                    Employee employee;
                    try {
                        UUID uuid = UUID.fromString(newEmployeeResponseDto.getId());
                        Optional<Employee> employeeMatch = employeeRepository.findById(uuid);
                        if (employeeMatch.isPresent()) {
                            employee = employeeMatch.get();
                            employee.setName(newEmployeeResponseDto.getName());
                            employee.setRole(newEmployeeResponseDto.getRole());
                            employee.setEmail(newEmployeeResponseDto.getEmail());
                        } else {
                            employee = employeeDtoMapper.toEmployee(newEmployeeResponseDto);
                        }
                    }
                    catch (IllegalArgumentException exception){
                        employee = employeeDtoMapper.toEmployee(newEmployeeResponseDto);
                    }
                    employeeRepository.save(employee);
                    return "Employee: "+employee.getId().toString()+" updated successfully";
                }
                else {
                    return "Employee: "+newEmployeeResponseDto.getId()+" update unsuccessful. Email provided is invalid";
                }
            }
            else {
                return "Employee: "+newEmployeeResponseDto.getId()+" update unsuccessful. Some or all of the Employee details provided are empty";
            }
        }
        else{
            return "Employee: "+newEmployeeResponseDto.getId()+" update unsuccessful. Some or all of the Employee details are not provided";
        }
    }

    public String deleteEmployee (String id) {
        try{
            UUID uuid = UUID.fromString(id);
            employeeRepository.deleteById(uuid);
            return "Employee: "+id+" deleted successfully";
        }
        catch (EmptyResultDataAccessException exception){
            throw new EmployeeNotFoundException(id);
        }
    }
}
