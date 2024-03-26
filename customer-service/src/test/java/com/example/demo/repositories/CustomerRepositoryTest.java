
package com.example.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test; //import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.entities.Customer;

@DataJpaTest
public class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test void itShouldFindCustomerFirstName() {
  
  // Given 
		Customer customer = Customer.builder() .firstName("Moud")
  .lastName("Diallo") .email("Moud@gmail.com") .build();
  
  // On insere un nouveau dans la base de données
  this.customerRepository.save(customer);
  
  // When
  
  // On recherche ce client par firstname 
  Optional<Customer> excepted =
  this.customerRepository.findByFirstName(customer.getFirstName());
  
  // Then
  
  // Comme findByFirstName retourne un Optional, on certifie qu'il nest pas nul
  assertThat(excepted).isPresent();
  
  // On certifie que le prénom du client recherché est le nom que celui qu'on ainséré
  assertThat(excepted.get().getFirstName()).isEqualTo(customer.getFirstName());
  
  // On certifie que le nom du client recherché est le nom que celui qu'on a inséré
  assertThat(excepted.get().getLastName()).isEqualTo(customer.getLastName());
  
  // On certifie que le numéro de téléphone du client recherché est le nom que celui qu'on a inséré
  assertThat(excepted.get().getEmail()).isEqualTo(customer.getEmail()); }

	@Test void itShouldNotFindCustomerWhenFirstNameDoesNotExist() {
		// Given //
  //On sait qu'aucun client n'a se numero 
		String fname = "ali";
  
  // When // On fait la recherche 
		Optional<Customer> customer =
  this.customerRepository.findByFirstName(fname);
  
  // Then // On certifie qu'il. n'existe aucun client avec ce numéro.
  assertThat(customer).isEmpty(); }

}
