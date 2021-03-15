package com.example.empmanager.service;


import com.example.empmanager.dto.EmployeeRequestDto;
import com.example.empmanager.dto.EmployeeResponseDto;
import com.example.empmanager.util.EmployeeDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeUpdateMessageReceiver {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${empmanager.rabbitmq.exchange}")
    private String exchange;

    @Value("${empmanager.rabbitmq.routingkey}")
    private String routingkey;

    private final EmployeeDtoMapper employeeDtoMapper = new EmployeeDtoMapper();

    private static final Logger log = LoggerFactory.getLogger(EmployeeUpdateMessageListener.class);

    public void send(EmployeeRequestDto employeeRequestDto, String id) {
        EmployeeResponseDto employeeResponseDto = employeeDtoMapper.toResponseDto(employeeRequestDto, id);
        log.info("Employee: "+id+" update message sent.");
        rabbitTemplate.convertAndSend(exchange, routingkey, employeeResponseDto);
    }
}