package com.example.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static  org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.BDDMockito.given;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.example.demo.entities.Customer;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.servicesimpl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

	/*
	 * @Autowired private ObjectMapper objectMapper;
	 */

	@Mock
	private CustomerRepository customerrepository;

	@InjectMocks
	private CustomerServiceImpl customerserviceimpl;

	
	
	@DisplayName("add customer")
	@Test
	public void shouldAddCustomer() throws Exception {
		// Mock data
		Customer customerToSave = new Customer("rrr", "Kacem", "rania@gmail.com");
		
		given(customerrepository.save(customerToSave)).willReturn(customerToSave);
         System.out.println(customerToSave);
		// Act
		Customer savedCustomer = customerserviceimpl.saveCustomer(customerToSave);

		// Assert
		assertEquals(customerToSave, savedCustomer);

		verify(customerrepository, times(1)).save(customerToSave);

	}
	
	
	@DisplayName("existing email")
	@Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
		
		
		Customer customer = new Customer("Rania", "Kacem", "rania@gmail.com");
		
		  given(customerrepository.findByEmail(customer.getEmail()))
                .willReturn(Optional.of(customer));
      

        // when -  action or the behaviour that we are going test
      assertThrows(ResourceNotFoundException.class, () -> {
        	customerserviceimpl.saveCustomer(customer);
        });

        // then
        verify(customerrepository, never()).save(any(Customer.class));
    }
	
	
	@DisplayName("first name empty")
	@Test
    public void givenEmptyFirstName_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
		
		
		Customer customer = new Customer("", "Kacem", "rania@gmail.com");
	
		 assertThrows(IllegalArgumentException.class, () -> {
			 customerserviceimpl.saveCustomer(customer);
	        });
      

        // then
        verify(customerrepository, never()).save(any(Customer.class));
    }
	
	@DisplayName("last name empty")
	@Test
    public void givenEmptyLasttName_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
		
		
		Customer customer = new Customer("Rania", "", "rania@gmail.com");
	
		 assertThrows(IllegalArgumentException.class, () -> {
			 customerserviceimpl.saveCustomer(customer);
	        });
      

        // then
        verify(customerrepository, never()).save(any(Customer.class));
    }
	
	
	@DisplayName("email empty")
	@Test
    public void givenEmptyEmail_whenSaveEmployee_thenThrowsException(){
        // given - precondition or setup
		
		
		Customer customer = new Customer("Rania", "Kacem", "");
	
		 assertThrows(IllegalArgumentException.class, () -> {
			 customerserviceimpl.saveCustomer(customer);
	        });
      

        // then
        verify(customerrepository, never()).save(any(Customer.class));
    }
	
	
	
	
	@DisplayName("return customers")
	@Test
	public void shouldReturnCustomers() throws Exception {

		// Mock data
		Customer customer1 = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");
		Customer customer2 = new Customer(1L, "Rim", "Batbout", "rim@gmail.com");
		List<Customer> customers = Arrays.asList(customer1, customer2);

		// Mock the repository behavior
		when(customerrepository.findAll()).thenReturn(customers);

		List<Customer> liste = customerserviceimpl.findAllCustomers();

		assertThat(liste).isNotNull();
		assertThat(liste.size()).isEqualTo(2);
		// Assert
		assertEquals(customers, liste);

		// Verify that the repository method was called
		verify(customerrepository, times(1)).findAll();
	}
	
	
	@DisplayName("return customer by id")
	@Test
	public void shouldReturnCustomersById() throws Exception {

		// Mock data
		Customer customer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");

		// Mock the repository behavior
		when(customerrepository.findById(1L)).thenReturn(Optional.of(customer));

		// given
		// given(customerrepository.findById(1L)).willReturn(Optional.of(customer));

		// when
		Customer savedCustomer = customerserviceimpl.findCustomerById(customer.getId()).get();

		// then
		assertThat(savedCustomer).isNotNull();
		assertEquals(savedCustomer, customer);

		// Verify that the repository method was called
		verify(customerrepository, times(1)).findById(1L);

	}
   
	@DisplayName("return customer by firstname")
	@Test
	public void shouldReturnCustomersByFirstName() throws Exception {

		// Mock data
		Customer customer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");

		// Mock the repository behavior
		when(customerrepository.findByFirstName("Rania")).thenReturn(Optional.of(customer));

		// when
		Customer savedCustomer = customerserviceimpl.findCustomerByFirstName(customer.getFirstName()).get();

		// then
		assertThat(savedCustomer).isNotNull();
		assertEquals(savedCustomer, customer);

		// Verify that the repository method was called
		verify(customerrepository, times(1)).findByFirstName("Rania");

	}
	
	@DisplayName("return customer by email")
	@Test
	public void shouldReturnCustomersByEmail() throws Exception {

		// Mock data
		Customer customer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");

		// Mock the repository behavior
		when(customerrepository.findByEmail("rania@gmail.com")).thenReturn(Optional.of(customer));

		// when
		Customer savedCustomer = customerserviceimpl.findCustomerByEmail(customer.getEmail()).get();

		// then
		assertThat(savedCustomer).isNotNull();
		assertEquals(savedCustomer, customer);

		// Verify that the repository method was called
		verify(customerrepository, times(1)).findByEmail("rania@gmail.com");

	}
	
	@DisplayName("d customer")

	  @Test
	    public void shouldDeleteCustomerSuccessfully() {
	        // Mock data
	        Long customerId = 1L;

	        // Call the method to be tested
	        customerserviceimpl.deleteCustomer(customerId);

	        // Verify that deleteById was called with the correct id
	        verify(customerrepository, times(1)).deleteById(customerId);
	    }
	
	
	
	
	@DisplayName("update customer")
	@Test
    public void shouldUpdateCustomer(){
		
		// Mock data
				Customer customer = new Customer(1L, "Rania", "Kacem", "rania@gmail.com");
        // given - precondition or setup
        given(customerrepository.save(customer)).willReturn(customer);
        
        
        customer.setEmail("ram@gmail.com");
        customer.setFirstName("Ram");
        customer.setLastName("ka");
        
        
        // when -  action or the behaviour that we are going test
        Customer updatedCustomer = customerserviceimpl.updateCustomer(customer);

        // then - verify the output
        assertThat(updatedCustomer.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedCustomer.getFirstName()).isEqualTo("Ram");
        assertThat(updatedCustomer.getLastName()).isEqualTo("ka");
    }
	


}
