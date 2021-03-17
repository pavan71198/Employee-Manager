package com.example.empmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class EmpmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpmanagerApplication.class, args);
	}

}
