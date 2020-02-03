package com.demo.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

@Controller
public class EmployeeConsumerController {
	
	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private LoadBalancerClient loadBalancer;


	@RequestMapping(value = "/employee/{empId}",
			method = RequestMethod.GET)
	@HystrixCommand(commandKey = "customCommandKey")

	public ResponseEntity getEmployee(@PathVariable String empId) throws RestClientException, IOException {

		/*** without load balancing ***/

		/*List<ServiceInstance> instances=discoveryClient.getInstances("employee-producer");
		ServiceInstance serviceInstance=instances.get(0);*/

		/*** without load balancing ***/


        /*** with load balancing ***/

		ServiceInstance serviceInstance=loadBalancer.choose("employee-producer");
        System.out.println("********************************************************************************");
		System.out.println("Employee Producer url selected:::::"+serviceInstance.getUri());
		System.out.println("********************************************************************************");


		/*** with load balancing ***/

		String baseUrl=serviceInstance.getUri().toString();
		
		baseUrl=baseUrl+"/employee/" + empId;
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		response=restTemplate.exchange(baseUrl,
				HttpMethod.GET, getHeaders(),String.class);

		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/healthCheck",
			method = RequestMethod.GET)

	public ResponseEntity healthCheck() throws RestClientException, IOException {

		ServiceInstance serviceInstance=loadBalancer.choose("employee-producer");
		System.out.println("********************************************************************************");
		System.out.println("Employee Producer url selected:::::"+serviceInstance.getUri());
		System.out.println("********************************************************************************");
		String baseUrl=serviceInstance.getUri().toString();
		baseUrl=baseUrl + "/healthCheck" ;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response=null;
		response=restTemplate.exchange(baseUrl,
				HttpMethod.GET, getHeaders(),String.class);

		return ResponseEntity.ok(response.toString());
	}

	private static HttpEntity<?> getHeaders() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
}