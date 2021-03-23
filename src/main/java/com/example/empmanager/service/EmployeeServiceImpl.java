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

    @Autowired
    private EmployeeRedisService employeeRedisService;

    private final EmployeeDtoMapper employeeDtoMapper = new EmployeeDtoMapper();

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern emailPattern = Pattern.compile(emailRegex);

    EmployeeResponseDto errorEmployeeResponseDto = new EmployeeResponseDto("-1","","","");

    public List<EmployeeResponseDto> fetchAllEmployees(){
        List<EmployeeResponseDto> employeeResponseDtoList = new ArrayList<>();
        List<Employee> employeeList = employeeRepository.findAll();
        if (!CollectionUtils.isEmpty(employeeList)) {
            for (Employee employee : employeeList) {
                EmployeeResponseDto employeeResponseDto = employeeDtoMapper.toResponseDto(employee);
                employeeRedisService.update(employeeResponseDto.getId(), employeeResponseDto);
                employeeResponseDtoList.add(employeeResponseDto);
            }
        }
        return employeeResponseDtoList;
    }

    public String addNewEmployee (EmployeeRequestDto newEmployeeRequestDto) {
        if (newEmployeeRequestDto.getName()!=null && newEmployeeRequestDto.getRole()!=null && newEmployeeRequestDto.getEmail()!=null) {
            if (!newEmployeeRequestDto.getName().isEmpty() && !newEmployeeRequestDto.getRole().isEmpty()) {
                if (emailPattern.matcher(newEmployeeRequestDto.getEmail()).matches()) {
                    Employee newEmployee = new Employee(newEmployeeRequestDto.getName(), newEmployeeRequestDto.getRole(), newEmployeeRequestDto.getEmail());
                    employeeRepository.save(newEmployee);
                    String id = newEmployee.getId().toString();
                    employeeRedisService.delete(id);
                    return "Saved: " + id;
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

    public EmployeeResponseDto fetchEmployeeById(String id) {
        try {
            EmployeeResponseDto cachedEmployeeResponseDto = employeeRedisService.get(id);
            if (cachedEmployeeResponseDto != null){
                if (cachedEmployeeResponseDto.getId().equals("-1")){
                    throw new EmployeeNotFoundException(id);
                }
                return cachedEmployeeResponseDto;
            }
            UUID uuid = UUID.fromString(id);
            Optional<Employee> employeeMatch = employeeRepository.findById(uuid);
            if (employeeMatch.isPresent()) {
                EmployeeResponseDto employeeResponseDto = employeeDtoMapper.toResponseDto(employeeMatch.get());
                employeeRedisService.update(id, employeeResponseDto);
                return employeeResponseDto;
            } else {
                employeeRedisService.update(id, errorEmployeeResponseDto);
                throw new EmployeeNotFoundException(id);
            }
        }
        catch (IllegalArgumentException exception){
            employeeRedisService.update(id, errorEmployeeResponseDto);
            throw new EmployeeNotFoundException(id);
        }
    }

    public String updateEmployee(EmployeeResponseDto newEmployeeRequestDto) {
        if (newEmployeeRequestDto.getName()!=null && newEmployeeRequestDto.getRole()!=null && newEmployeeRequestDto.getEmail()!=null) {
            if (!newEmployeeRequestDto.getName().isEmpty() && !newEmployeeRequestDto.getRole().isEmpty()) {
                if (emailPattern.matcher(newEmployeeRequestDto.getEmail()).matches()) {
                    Employee employee;
                    try {
                        UUID uuid = UUID.fromString(newEmployeeRequestDto.getId());
                        Optional<Employee> employeeMatch = employeeRepository.findById(uuid);
                        if (employeeMatch.isPresent()) {
                            employeeRedisService.delete(newEmployeeRequestDto.getId());
                            employee = employeeMatch.get();
                            employee.setName(newEmployeeRequestDto.getName());
                            employee.setRole(newEmployeeRequestDto.getRole());
                            employee.setEmail(newEmployeeRequestDto.getEmail());
                        } else {
                            employeeRedisService.update(newEmployeeRequestDto.getId(), errorEmployeeResponseDto);
                            employee = employeeDtoMapper.toEmployee(newEmployeeRequestDto);
                        }
                    } catch (IllegalArgumentException exception) {
                        employeeRedisService.update(newEmployeeRequestDto.getId(), errorEmployeeResponseDto);
                        employee = employeeDtoMapper.toEmployee(newEmployeeRequestDto);
                    }
                    employeeRepository.save(employee);
                    return "Employee: "+employee.getId().toString()+" updated successfully";
                }
                else {
                    return "Employee: "+newEmployeeRequestDto.getId()+" update unsuccessful. Email provided is invalid";
                }
            }
            else {
                return "Employee: "+newEmployeeRequestDto.getId()+" update unsuccessful. Some or all of the Employee details provided are empty";
            }
        }
        else{
            return "Employee: "+newEmployeeRequestDto.getId()+" update unsuccessful. Some or all of the Employee details are not provided";
        }
    }


    public String deleteEmployee (String id) {
        try{
            UUID uuid = UUID.fromString(id);
            employeeRepository.deleteById(uuid);
            employeeRedisService.delete(id);
            return "Employee: "+id+" deleted successfully";
        }
        catch (EmptyResultDataAccessException exception){
            employeeRedisService.update(id, errorEmployeeResponseDto);
            throw new EmployeeNotFoundException(id);
        }
    }
}
