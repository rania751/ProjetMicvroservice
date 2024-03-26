package com.example.demo.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.BDDMockito.given;
import org.springframework.test.web.servlet.ResultActions;

import com.example.demo.entities.Customer;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

//@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CustomerRestController.class)
public class CustomerRestControllerTest {

	@Autowired
	private MockMvc mockmvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CustomerRepository customerrepository;
	@MockBean
	private CustomerService customerservice;

	@InjectMocks
	private CustomerRestController customerRestController;

	@Test
	public void shouldAddCustomer() throws Exception {
		// Mock data
		Customer customerToSave = new Customer("Rania", "Kacem", "rania@gmail.com");
		Customer savedCustomer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");

		// Mock the repository behavior
		when(customerservice.saveCustomer(any(Customer.class))).thenReturn(savedCustomer);

		// Perform the POST request
		mockmvc.perform(MockMvcRequestBuilders.post("/customers").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(customerToSave)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Rania"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Kacem"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("rania@gmail.com"));

		// Verify that the save method was called with the correct argument
		verify(customerservice).saveCustomer(any(Customer.class));
	}

	@Test
	public void shouldReturnCustomers() throws Exception {

		// Mock data
		Customer customer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");
		List<Customer> customers = Arrays.asList(customer);

		// Mock the repository behavior
		when(customerservice.findAllCustomers()).thenReturn(customers);

		// Perform the GET request

		mockmvc.perform(get("/customers")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].firstName").value("Rania"))
				.andExpect(jsonPath("$[0].lastName").value("Kacem"))
				.andExpect(jsonPath("$[0].email").value("rania@gmail.com"));

		// Verify that the repository method was called
		verify(customerservice, times(1)).findAllCustomers();
	}

	
	
	
	@Test
	public void shouldReturnCustomersById() throws Exception {

		// Mock data
		Customer customer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");

		// Mock the repository behavior
		when(customerservice.findCustomerById(1L)).thenReturn(Optional.of(customer));

		// Perform the GET request

		mockmvc.perform(get("/customers/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.firstName").value("Rania")).andExpect(jsonPath("$.lastName").value("Kacem"))
				.andExpect(jsonPath("$.email").value("rania@gmail.com"));

		// Verify that the repository method was called
		verify(customerservice, times(1)).findCustomerById(1L);

	}



	@Test
	public void shouldReturnCustomersByFirstName() throws Exception {

		// Mock data
		Customer customer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");

		// Mock the repository behavior
		when(customerservice.findCustomerByFirstName("Rania")).thenReturn(Optional.of(customer));

		// Perform the GET request

		mockmvc.perform(get("/customers/firstname:/Rania")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.firstName").value("Rania"))
				.andExpect(jsonPath("$.lastName").value("Kacem"))
				.andExpect(jsonPath("$.email").value("rania@gmail.com"));

		// Verify that the repository method was called
		verify(customerservice, times(1)).findCustomerByFirstName("Rania");

	}
	
	
    @Test
    public void shouldThrowExceptionForNonExistingCustomerByFirstName() throws Exception {
        // Mock data
        String nonExistingFirstName = "NonExisting";

        // Mock the service behavior for a non-existing customer
        when(customerservice.findCustomerByFirstName(nonExistingFirstName)).thenReturn(Optional.empty());

        // Perform the GET request and expect a 404 status
        mockmvc.perform(get("/customers/firstname:/{firstname}", nonExistingFirstName))
            .andExpect(status().isNotFound());
    }

	
	
	@Test
	public void shouldReturnCustomersByEmail() throws Exception {

		// Mock data
		Customer customer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");

		// Mock the repository behavior
		when(customerservice.findCustomerByEmail("rania@gmail.com")).thenReturn(Optional.of(customer));

		// Perform the GET request

		mockmvc.perform(get("/customers/email:/rania@gmail.com")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.firstName").value("Rania"))
				.andExpect(jsonPath("$.lastName").value("Kacem"))
				.andExpect(jsonPath("$.email").value("rania@gmail.com"));

		// Verify that the repository method was called
		verify(customerservice, times(1)).findCustomerByEmail("rania@gmail.com");

	}
    @Test
    public void shouldThrowExceptionForNonExistingCustomerByEmail() throws Exception {
        // Mock data
        String nonExistingEmail = "NonExisting@gmail.com";

        // Mock the service behavior for a non-existing customer
        when(customerservice.findCustomerByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        // Perform the GET request and expect a 404 status
        mockmvc.perform(get("/customers/email:/{email}", nonExistingEmail))
            .andExpect(status().isNotFound());
    }

	
	
	// JUnit test for update employee REST API - positive scenario
	@Test
	public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
		// given - precondition or setup
		long customerId = 1L;
		Customer savedCustomer = Customer.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com")
				.build();

		Customer updatedCustomer = Customer.builder().firstName("Ram").lastName("Jadhav").email("ram@gmail.com")
				.build();
		given(customerservice.findCustomerById(customerId)).willReturn(Optional.of(savedCustomer));
		given(customerservice.updateCustomer(any(Customer.class)))
				.willAnswer((invocation) -> invocation.getArgument(0));

		// when - action or the behaviour that we are going test
		ResultActions response = mockmvc.perform(put("/customers/{id}", customerId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updatedCustomer)));

		// then - verify the output
		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.firstName", is(updatedCustomer.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedCustomer.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedCustomer.getEmail())));
	}
	
	
	  @Test
	    public void shouldDeleteCustomerSuccessfully() throws Exception {
	        // Mock data
	        Long customerId = 1L;

	        // Mock the service behavior for an existing customer
	        when(customerservice.findCustomerById(customerId)).thenReturn(Optional.of(new Customer()));

	        // Perform the DELETE request and expect a 200 status
	        mockmvc.perform(delete("/customers/{id}", customerId))
	            .andExpect(status().isOk());

	        // Verify that the deleteCustomer method was called with the correct id
	        verify(customerservice, times(1)).deleteCustomer(customerId);
	    }

	    @Test
	    public void shouldThrowExceptionForNonExistingCustomerToDelete() throws Exception {
	        // Mock data
	        Long nonExistingCustomerId = 2L;

	        // Mock the service behavior for a non-existing customer
	        when(customerservice.findCustomerById(nonExistingCustomerId)).thenReturn(Optional.empty());

	        // Perform the DELETE request and expect a 404 status
	        mockmvc.perform(delete("/customers/{id}", nonExistingCustomerId))
	            .andExpect(status().isNotFound());
	            //.andExpect(jsonPath("$.message").value("Customer does not exist with this id " + nonExistingCustomerId));
	    }

}
