package com.example.demo.servicesimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.BankAccount;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.services.AccountService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

	private BankAccountRepository accountRepository;

	public AccountServiceImpl(BankAccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public BankAccount saveAccount(BankAccount account) {

		return this.accountRepository.save(account);

	}

	public List<BankAccount> findAllAccounts() {
		return this.accountRepository.findAll();
	}

	public Optional<BankAccount> findAccountById(Long id) {
		return this.accountRepository.findById(id);
	}

	public BankAccount updateAccount(BankAccount account) {
		return accountRepository.save(account);
	}

	public void deleteAccount(Long id) {
		accountRepository.deleteById(id);
	}

	public List<BankAccount> findAccountByCustomerId(Long id) {
		return this.accountRepository.findByCustomerId(id);
	}

	@Transactional
	public void deleteAccountsByCustomerId(Long id) {
		accountRepository.deleteAllByCustomerId(id);
		log.info("The list of accounts of the customer with id {} is deleted sucsessfully",id);

	}

}
