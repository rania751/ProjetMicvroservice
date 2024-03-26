package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Customer;

public interface CustomerService {
	public Customer saveCustomer(Customer customer);

	public List<Customer> findAllCustomers();

	public Optional<Customer> findCustomerById(Long id);

	public Optional<Customer> findCustomerByFirstName(String fname);

	public Optional<Customer> findCustomerByEmail(String email);

	public Customer updateCustomer(Customer updatedCustomer);

	public void deleteCustomer(Long id);
}
