package com.example.empmanager.service.listener;

import com.example.empmanager.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.empmanager.dto.EmployeeResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmployeeUpdateListener {
    @Autowired
    private EmployeeService employeeService;

    private static final Logger log = LoggerFactory.getLogger(EmployeeUpdateListener.class);

    @KafkaListener(topics = "${empmanager.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "employeeKafkaListenerContainerFactory")
    public void receiveMessage(EmployeeResponseDto newEmployeeResponseDto) {
        String response = employeeService.updateEmployee(newEmployeeResponseDto);
        log.info(response);
    }
}
