package com.example.empmanager.config;

import com.example.empmanager.dto.EmployeeResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    String host;
    @Value("${spring.redis.port}")
    int port;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, EmployeeResponseDto> redisTemplate() {
        RedisTemplate<String, EmployeeResponseDto> redisTemplate = new RedisTemplate<String, EmployeeResponseDto>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }



}