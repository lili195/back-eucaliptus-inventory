package com.eucaliptus.springboot_app_products;

import com.eucaliptus.springboot_app_products.SpringbootAppProductsApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringbootAppProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAppProductsApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
