package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.clients.CustomerRestClient;
import com.example.demo.entities.BankAccount;
import com.example.demo.models.Customer;
import com.example.demo.services.AccountService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountRestController {
	private CustomerRestClient customerRestClient;
	private AccountService accountService;

	public AccountRestController(CustomerRestClient customerRestClient, AccountService accountService) {
		this.customerRestClient = customerRestClient;
		this.accountService = accountService;
	}
	
	

	@PostMapping
	public BankAccount addCustomer(@RequestBody BankAccount account) {
		return accountService.saveAccount(account);
	}

	@GetMapping
	public List<BankAccount> accountList() {
		log.info("return all accounts");
		List<BankAccount> accountList = accountService.findAllAccounts();
		log.info("accounts founded");
		accountList.forEach(acc -> {
			log.info("add the next");
			Long id = acc.getCustomerId();
			log.info("add the customer with the id:{}", id);
			acc.setCustomer(customerRestClient.findCustomerById(id));
			log.info("customer added successfully");

		});
		return accountList;
	}

	@GetMapping("/list/{id}")
	public List<BankAccount> bankAccountsById(@PathVariable Long id) {

		List<BankAccount> bankAccounts = accountService.findAccountByCustomerId(id);

		return bankAccounts;
	}

	@GetMapping("/{id}")
	public BankAccount bankAccountById(@PathVariable Long id) {

		BankAccount bankAccount = accountService.findAccountById(id).get();
		log.info("Account founded by the id :{}", id);
		log.info("The customer with this account has the id :{}", bankAccount.getCustomerId());
		Customer customer = customerRestClient.findCustomerById(bankAccount.getCustomerId());
		log.info("Customer founded");
		bankAccount.setCustomer(customer);
		return bankAccount;
	}


	@PutMapping("/{id}")
	public Optional<BankAccount> updateCustomer(@PathVariable("id") Long accountId, @RequestBody BankAccount account) {
		return accountService.findAccountById(accountId).map(savedAccount -> {
			savedAccount.setBalance(account.getBalance());
			savedAccount.setCreateAt(account.getCreateAt());
			savedAccount.setCurrency(account.getCurrency());
			savedAccount.setType(account.getType());
			savedAccount.setCustomer(account.getCustomer());
			savedAccount.setCustomerId(account.getCustomerId());
			BankAccount updatedAccount = accountService.updateAccount(savedAccount);
			return (updatedAccount);
		});
	}
	 
	 

	@DeleteMapping("/customer/{id}")
	public void deleteAccountbycustomer(@PathVariable Long id) {
		this.accountService.deleteAccountsByCustomerId(id);
		
	}
	
	
	

	@DeleteMapping("/{id}")
	public void deleteAccount(@PathVariable Long id) {
		log.info("Deleting the customer with id: {}", id);
		Optional<BankAccount> accountToDelete = accountService.findAccountById(id);
		if (accountToDelete.isPresent())

		{
			accountService.deleteAccount(id);
			log.info("Account with id {} deleted successfully", id);
		}

		else {
			throw new ResourceNotFoundException("Account does not exist withthis id " + id);

		}
	}

}