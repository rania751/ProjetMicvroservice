package com.example.demo;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.example.demo.entities.Customer;
import com.example.demo.repositories.CustomerRepository;

@SpringBootApplication
@EnableFeignClients
public class CustomerServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(CustomerServiceApplication.class, args);

	}

	@Bean
	CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
		return args -> {

			List<Customer> customerList = List.of(
					Customer.builder().email("hassan@gmail.com").firstName("Hassan").lastName("Elhoumi")

							.build(),

					Customer.builder().email("Mohamed@gmail.com").firstName("Mohamed").lastName("Elhannaoui").build(),

					Customer.builder().email("Ali@gmail.com").firstName("Ali").lastName("Ezzine").build()

			);

			customerRepository.saveAll(customerList);
		};
	}

}