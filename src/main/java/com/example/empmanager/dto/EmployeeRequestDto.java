package com.example.empmanager.dto;

public class EmployeeRequestDto {
    private String name;
    private String role;
    private String email;

    public EmployeeRequestDto(String name, String role, String email) {
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public EmployeeRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
