package com.example.demo.services;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.entities.Customer;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.servicesimpl.CustomerServiceImpl;

/** Integration tests for {@link CustomerServiceImpl} */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class CustomerServiceImplIt {

	private CustomerServiceImpl underTest;

	private CustomerRepository customerRepository;

	public CustomerServiceImplIt(CustomerServiceImpl underTest, CustomerRepository customerRepository) {
		this.underTest = underTest;
		this.customerRepository = customerRepository;
	}

	@Test
	public void testSaveCustomer() {
		// Arrange
		Customer customer = new Customer();
		customer.setFirstName("John");
		customer.setLastName("Doe");
		customer.setEmail("john.doe@example.com");

		when(customerRepository.findByEmail(customer.getEmail())).thenReturn(null);
		when(customerRepository.save(customer)).thenReturn(customer);

		// Act
		Customer savedCustomer = underTest.saveCustomer(customer);
		System.out.println(savedCustomer);
		// Assert
		assert savedCustomer.getEmail().equals(customer.getEmail());
		/*
		 * assert savedCustomer != null;
		 * 
		 */
		// Add more assertions based on your specific business logic and expectations
	}
	/*
	 * @Test public void testSaveCustomer() { // Arrange Customer customer = new
	 * Customer(); customer.setFirstName("John"); customer.setLastName("Doe");
	 * customer.setEmail("john.doe@example.com");
	 * 
	 * when(customerRepository.findByEmail(customer.getEmail())).thenReturn(null);
	 * when(customerRepository.save(customer)).thenReturn(customer);
	 * 
	 * // Act Customer savedCustomer = underTest.saveCustomer(customer);
	 * 
	 * // Assert assert savedCustomer != null; assert
	 * savedCustomer.getEmail().equals(customer.getEmail()); // Add more assertions
	 * based on your specific business logic and expectations }
	 */

}
