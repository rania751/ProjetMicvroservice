package com.example.demo.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.models.Account;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountRestClient {

	@DeleteMapping("/accounts/customer/{id}")
	@CircuitBreaker(name = "accountService")
	@Retry(name = "accountService", fallbackMethod = "deletAccounts")
	void deleteAccountsByCustomerId(@PathVariable Long id);

	@GetMapping("/accounts/list/{id}")
	@CircuitBreaker(name = "accountService")
	@Retry(name = "accountService", fallbackMethod = "getAccounts")
	List<Account> findAccountsCustomerById(@PathVariable Long id);

	default void deletAccounts(Exception exception) {
		System.out.println("Something goes wrong, Deleting accounts is not possible now" + exception.getMessage());
	}

	default List<Account> getAccounts(Exception exception) {
		// Log the exception
		System.out.println("Fallback - getAllCustomers: " + exception.getMessage());
		Account notvailable = new Account(null, 0, null, null, null, null, null);
		return List.of(notvailable);
	}

}