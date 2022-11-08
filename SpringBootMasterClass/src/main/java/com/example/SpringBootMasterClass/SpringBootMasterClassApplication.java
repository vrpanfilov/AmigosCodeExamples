package com.example.SpringBootMasterClass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringBootMasterClassApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootMasterClassApplication.class, args);
	}

}
