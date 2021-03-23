package com.example.empmanager.service;

import com.example.empmanager.dto.EmployeeResponseDto;
import com.example.empmanager.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class EmployeeRedisService {
    @Autowired
    private RedisTemplate<String, EmployeeResponseDto> redisTemplate;
    @Resource(name="redisTemplate")
    private ValueOperations<String, EmployeeResponseDto> valueOperations;
    @Value("${spring.redis.employee.ttl}")
    private long ttl;

    public void update(String id, EmployeeResponseDto employeeResponseDto){
        valueOperations.set(id, employeeResponseDto, ttl, TimeUnit.MINUTES);
    }

    public void delete(String id){
        redisTemplate.delete(id);
    }

    public EmployeeResponseDto get(String id){
        return valueOperations.get(id);
    }
}
