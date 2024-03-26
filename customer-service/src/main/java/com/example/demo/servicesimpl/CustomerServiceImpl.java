package com.example.demo.servicesimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entities.Customer;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.services.CustomerService;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepository;

	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Customer saveCustomer(Customer customer) {

		try {
			customerRepository.findByEmail(customer.getEmail());

			if (StringUtils.isEmpty(customer.getFirstName())) {
				log.error("FirstName cannot be empty");
				throw new IllegalArgumentException("Required Field cannot be empty");
			}

			if (StringUtils.isEmpty(customer.getLastName())) {
				log.error("LastName cannot be empty");
				throw new IllegalArgumentException("Required Field cannot be empty");
			}

			if (StringUtils.isEmpty(customer.getEmail())) {
				log.error("Email cannot be empty");
				throw new IllegalArgumentException("Required Field cannot be empty");

			}

			return this.customerRepository.save(customer);

		} catch (IllegalArgumentException ex) {
			log.error(ex.getMessage());
			throw new IllegalArgumentException("Required Field cannot be empty");

		} catch (Exception e) {
			log.error("Existing Email " + customer.getEmail() + "Customer can Not be added try with another Email");
			throw new EntityExistsException("Existing Email ");
		}

	}

	public List<Customer> findAllCustomers() {

		log.info("Return all the customers");
		List<Customer> lcustomers = this.customerRepository.findAll();
		int len = lcustomers.size();
		log.info("{} customers are founded ", len);
		return this.customerRepository.findAll();
	}

	public Optional<Customer> findCustomerById(@PathVariable Long id) {
		
		
		
		log.info("find the customer with id {}", id);

			Optional<Customer> customer = customerRepository.findById(id);
		if (customer.isPresent()) {
			return customer;}

		
		else {
			throw new ResourceNotFoundException("Customer with this id :" + id + " not found ");
			

		}
		
	}
	

	public Optional<Customer> findCustomerByEmail(String email) {

		Optional<Customer> customer = customerRepository.findByEmail(email);
		if (customer.isPresent()) {
			return customer;
		} else {
			throw new ResourceNotFoundException("Customer  with this email:" + email + "not found");
		}
	}

	public Optional<Customer> findCustomerByFirstName(String fname) {

		Optional<Customer> customer = customerRepository.findByFirstName(fname);
		if (customer.isPresent()) {
			return customer;
		} else {
			throw new ResourceNotFoundException("Customer  with this First Name:" + fname + "not found");
		}
	}

	public Customer updateCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	public void deleteCustomer(Long id) {

		if (findCustomerById(id).isPresent()) {
			customerRepository.deleteById(id);
			log.info("Customer with id {} deleted successfully", id);
		} else {
			throw new ResourceNotFoundException("Customer not found with this id:" + id);
		}

	}
}
