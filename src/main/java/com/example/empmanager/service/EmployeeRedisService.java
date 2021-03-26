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
    private HashOperations<String, String, EmployeeResponseDto> dataHashOperations;
    @Resource(name="redisTemplate")
    private HashOperations<String, String, Long> ttlHashOperations;
    private final String DATAKEY = "EMPLOYEES";
    private final String TTLKEY = "EMPLOYEES_TTL";
    @Value("${spring.redis.employee.ttl}")
    private Long ttl;

    public void update(String id, EmployeeResponseDto employeeResponseDto){
        dataHashOperations.put(DATAKEY, id, employeeResponseDto);
        ttlHashOperations.put(TTLKEY, id, System.currentTimeMillis()+ttl*60*1000);
    }

    public void delete(String id){
        dataHashOperations.delete(DATAKEY, id);
        ttlHashOperations.delete(TTLKEY, id);
    }

    public EmployeeResponseDto get(String id){
        Long cacheTtl = ttlHashOperations.get(TTLKEY, id);
        if (cacheTtl != null) {
            if (cacheTtl < System.currentTimeMillis()) {
                dataHashOperations.delete(DATAKEY, id);
                ttlHashOperations.delete(TTLKEY, id);
                return null;
            }
            else{
                return dataHashOperations.get(DATAKEY, id);
            }
        }
        return null;
    }
}
