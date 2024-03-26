package com.example.demo.controllers;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.entities.Customer;
import com.example.demo.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CustomerRestControllerTestIT {
	@Autowired
	private MockMvc mockMvc;

	/*
	 * @Autowired private WebApplicationContext context;
	 */

	/*
	 * @BeforeEach public void setup() { mockMvc =
	 * MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	 * }
	 */

	/*
	 * @Before() public void setUp(){ objectMapper = new ObjectMapper(); }
	 */

	@Autowired
	private CustomerRepository customerRepository;

	Customer customer = new Customer(1L, "John", "Doe", "John@gmail.com");

	@WithMockUser(username = "user", roles = { "USER" })
	@Test
	void fetchAllCustomersEndpoint() throws Exception {
		mockMvc.perform(get("/customers").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		assertThat(customerRepository.findAll()).hasSize(3);

	}

	
	
	@WithMockUser(username = "user", roles = { "USER" })
	@Test
	void fetchCustomerByFirstNameEndpoint() throws Exception {

		mockMvc.perform(get("/customers/firstname:/Hassan").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		//assert (customerRepository.findByFirstName("Hassan").isPresent());
	}

	@WithMockUser(username = "user", roles = { "USER" })
	@Test
	void fetchCustomerByIdEndpoint() throws Exception {

		mockMvc.perform(get("/customers/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		assert (customerRepository.findById(1L).isPresent());
	}
	
	
	@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
	@Test
	void updateCustomerEndpoint() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mockMvc.perform(put("/customers/1").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(customer)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}

	
	
	@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
	@Test
	void saveCustomerEndpoint() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(customer)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

	}
}