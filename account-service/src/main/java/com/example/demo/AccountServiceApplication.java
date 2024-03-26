package com.example.demo;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import com.example.demo.clients.CustomerRestClient;
import com.example.demo.entities.BankAccount;
import com.example.demo.enums.AccountType;
import com.example.demo.repositories.BankAccountRepository;

@SpringBootApplication
@EnableFeignClients
public class AccountServiceApplication {
	static final Logger logger = LoggerFactory.getLogger(AccountServiceApplication.class);

	public static void main(String[] args) {

		logger.info("Before Starting AccountServiceApplication");
		SpringApplication.run(AccountServiceApplication.class, args);
		logger.debug("Starting my AccountServiceApplication in debug with {} arguments", args.length);
		logger.info("Starting my AccountServiceApplication with {} arguments.", args.length);

	}

	
	/*
	 * @Bean CommandLineRunner commandLineRunner(BankAccountRepository
	 * accountRepository, CustomerRestClient customerRestClient) { return args -> {
	 * customerRestClient.allCustomers().forEach(c -> { BankAccount bankAccount1 =
	 * BankAccount.builder()
	 * 
	 * .currency("MAD").balance(Math.random() * 80000).createAt(LocalDate.now())
	 * .type(AccountType.CURRENT_ACCOUNT).customerId(c.getId()).build(); BankAccount
	 * bankAccount2 = BankAccount.builder()
	 * 
	 * .currency("MAD").balance(Math.random() * 65432).createAt(LocalDate.now())
	 * .type(AccountType.SAVING_ACCOUNT).customerId(c.getId()).build();
	 * 
	 * accountRepository.save(bankAccount1); accountRepository.save(bankAccount2);
	 * });
	 * 
	 * }; }
	 */
	 
}
