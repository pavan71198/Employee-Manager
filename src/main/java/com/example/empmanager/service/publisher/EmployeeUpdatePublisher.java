package com.example.empmanager.service.publisher;


import com.example.empmanager.dto.EmployeeRequestDto;
import com.example.empmanager.dto.EmployeeResponseDto;
import com.example.empmanager.service.listener.EmployeeUpdateListener;
import com.example.empmanager.util.EmployeeDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmployeeUpdatePublisher {

    @Value("${empmanager.kafka.topic}")
    private String TOPIC;

    @Autowired
    private KafkaTemplate<String, EmployeeResponseDto> employeeKafkaTemplate;

    private final EmployeeDtoMapper employeeDtoMapper = new EmployeeDtoMapper();

    private static final Logger log = LoggerFactory.getLogger(EmployeeUpdateListener.class);

    public void send(EmployeeRequestDto employeeRequestDto, String id) {
        EmployeeResponseDto employeeResponseDto = employeeDtoMapper.toResponseDto(employeeRequestDto, id);
        employeeKafkaTemplate.send(TOPIC, employeeResponseDto);
        log.info("Employee: "+id+" update message sent.");
    }
}