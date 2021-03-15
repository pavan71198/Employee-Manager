package com.example.empmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.empmanager.dto.EmployeeResponseDto;
import com.example.empmanager.entity.Employee;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class EmployeeUpdateMessageListener {
    @Autowired
    private EmployeeService employeeService;

    private static final Logger log = LoggerFactory.getLogger(EmployeeUpdateMessageListener.class);

    @RabbitListener(queues = "${empmanager.rabbitmq.queue}")
    public void receiveMessage(EmployeeResponseDto newEmployeeResponseDto) {
        String response = employeeService.updateEmployee(newEmployeeResponseDto);
        log.info(response);
    }
}
