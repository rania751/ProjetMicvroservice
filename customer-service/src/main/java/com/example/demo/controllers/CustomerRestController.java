package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
import com.example.demo.clients.AccountRestClient;
import com.example.demo.entities.Customer;
import com.example.demo.models.Account;
import com.example.demo.services.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerRestController {

	private CustomerService customerService;
	private AccountRestClient accountRestClient;

	public CustomerRestController(CustomerService customerService, AccountRestClient accountRestClient) {

		this.customerService = customerService;
		this.accountRestClient = accountRestClient;
	}

	// @PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping
	public Customer addCustomer(@RequestBody Customer customer) {
		return customerService.saveCustomer(customer);
	}

	@GetMapping
	public List<Customer> customerList() {

		return customerService.findAllCustomers();
	}

	@GetMapping("/{id}")
	public Optional<Customer> customerById(@PathVariable Long id) {

		try {
			return customerService.findCustomerById(id);
		} catch (ResourceNotFoundException e) {
			log.error(e.getMessage());
			throw new ResourceNotFoundException("Customer with this id" + id + "is not found ");
		}
	}

	@GetMapping("/firstname:/{firstname}")
	public Optional<Customer> customerByFirstName(@PathVariable String firstname) {
		try {
			return customerService.findCustomerByFirstName(firstname);
		} catch (ResourceNotFoundException e) {
			log.error(e.getMessage());
			throw new ResourceNotFoundException("Customer with firstname " + firstname + "is not found ");
		}
	}

	@GetMapping("/email:/{email}")
	public Optional<Customer> customerByEmail(@PathVariable String email) {
		try {
			return customerService.findCustomerByEmail(email);
		} catch (ResourceNotFoundException e) {
			log.error(e.getMessage());
			throw new ResourceNotFoundException("Customer with email" + email + "is not found ");
		}
	}

	@PutMapping("/{id}")
	public Optional<Customer> updateCustomer(@PathVariable("id") long customerId, @RequestBody Customer customer) {
		return customerService.findCustomerById(customerId)

				.map(savedCustomer -> {
					savedCustomer.setFirstName(customer.getFirstName());
					savedCustomer.setLastName(customer.getLastName());
					savedCustomer.setEmail(customer.getEmail());

					Customer updatedCustomer = customerService.updateCustomer(savedCustomer);
					return (updatedCustomer);
				});
	}

	/*
	 * @DeleteMapping("/{id}") public void deleteCustomer(@PathVariable Long id) {
	 * 
	 * customerService.deleteCustomer(id);
	 * 
	 * }
	 */

	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable Long id) {

		log.info("Checking if customer with id {} exist ", id);

		try {

			customerService.findCustomerById(id);
			log.info("Customer with id {} exist", id);
			log.info("Checking the availability of the Acccounts service ");
			List<Account> accountsByCustomer = accountRestClient.findAccountsCustomerById(id);
			if (!accountsByCustomer.isEmpty() && (accountsByCustomer.get(0).getAccountId() != null)) {

				log.info("Acccounts service is available and the customer has a list of accounts");
				customerService.deleteCustomer(id);

				log.info("Deleting the list of accounts");
				accountRestClient.deleteAccountsByCustomerId(id);

				List<Account> accountsByCustomerdeleted = accountRestClient.findAccountsCustomerById(id);

				if (accountsByCustomerdeleted.isEmpty()) {
					log.info("The list of accounts of the customer with id {} is deleted sucsessfully", id);
				}

			}

			if (!accountsByCustomer.isEmpty() && (accountsByCustomer.get(0).getAccountId() == null)) {
				log.error("Acccounts service is not available, Customer with id {} can not be deleted now ", id);

				throw new Exception("Acccounts service is not available Now");

			}

			if (accountsByCustomer.isEmpty()) {

				log.info("The existing customer with id {} has no accounts", id);
				log.info("Deleting this customer");
				customerService.deleteCustomer(id);

			}

		} catch (ResourceNotFoundException e) {

			throw new ResourceNotFoundException("Not existing customer");
		} catch (Exception e) {

			throw new ResourceNotFoundException(e.getMessage());
		}

	}

}