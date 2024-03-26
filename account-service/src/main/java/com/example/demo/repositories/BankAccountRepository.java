package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

	List<BankAccount> findByCustomerId(Long id);
	void deleteAllByCustomerId(Long id);
}