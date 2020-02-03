package com.demo;

import com.demo.controllers.EmployeeConsumerController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
public class EmployeeConsumerStarter {

	public static void main(String[] args) throws RestClientException, IOException {
		SpringApplication.run(
				EmployeeConsumerStarter.class, args);
		
	}
	
	/*@Bean
	public EmployeeConsumerController employeeConsumerController()
	{
		return  new EmployeeConsumerController();
	}*/
}
