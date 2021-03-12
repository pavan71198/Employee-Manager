package com.example.empmanager.controller;

import java.util.List;
import java.util.UUID;

import com.example.empmanager.dto.EmployeeRequestDto;
import com.example.empmanager.dto.EmployeeResponseDto;
import com.example.empmanager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(path="/add")
    String addNewEmployee (@RequestBody EmployeeRequestDto newEmployeeRequestDto) {
        return employeeService.addNewEmployee(newEmployeeRequestDto);
    }

    @GetMapping(path="/fetch/all")
    List<EmployeeResponseDto> fetchAllEmployees() {
        return employeeService.fetchAllEmployees();
    }

    @GetMapping(path="/fetch/{id}")
    EmployeeResponseDto getEmployeeById(@PathVariable String id) {
        return employeeService.fetchEmployeeById(id);
    }

    @PutMapping(path="/update/{id}")
    String updateEmployee(@RequestBody EmployeeRequestDto newEmployeeRequestDto, @PathVariable String id) {
        return employeeService.updateEmployee(newEmployeeRequestDto, id);
    }

    @DeleteMapping(path="/delete/{id}")
    String deleteEmployee (@PathVariable String id) {
        return employeeService.deleteEmployee(id);
    }
}